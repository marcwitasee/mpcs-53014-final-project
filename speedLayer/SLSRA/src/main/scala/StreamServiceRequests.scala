import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Increment
import org.apache.hadoop.hbase.util.Bytes
import org.joda.time.DateTime

object StreamServiceRequests {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  val hbaseConf: Configuration = HBaseConfiguration.create()
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  hbaseConf.set("hbase.zookeeper.quorum", "localhost")
  
  val hbaseConnection = ConnectionFactory.createConnection(hbaseConf)
  val totalServiceCalls = hbaseConnection.getTable(TableName.valueOf("mtrichardson_crime_calls_by_comm_area"))
  val serviceCallsByComm = hbaseConnection.getTable(TableName.valueOf("mtrichardson_sr_type_by_comm"))
  val serviceCallDeltas = hbaseConnection.getTable(TableName.valueOf("mtrichardson_avg_delta_dept"))
  val openServiceCalls = hbaseConnection.getTable(TableName.valueOf("mtrichardson_open_calls_dept"))
  
  def incrementDeltas(ksrr : KafkaServiceRequestRecord) : String = {

    if (ksrr.closedDate != null) {
      println(ksrr.closedDate)
      val createdTime = DateTime.parse(ksrr.createdDate)
      val closedTime = DateTime.parse(ksrr.closedDate)
      val delta = (closedTime.getMillis() / 1000) - (createdTime.getMillis() / 1000)
      val inc = new Increment(Bytes.toBytes(ksrr.ownerDepartment))
      inc.addColumn(Bytes.toBytes("deltas"), Bytes.toBytes("delta"), delta)
      inc.addColumn(Bytes.toBytes("deltas"), Bytes.toBytes("calls"), 1)
      serviceCallDeltas.increment(inc)
      "Deltas - Updated average deltas for " + ksrr.srNumber + " - " + ksrr.ownerDepartment
    } else {
      "Deltas - Service Request has not yet closed... No delta to update - " + ksrr.srNumber + " " + ksrr.ownerDepartment
    }
  }

  def incrementTotalCalls(ksrr : KafkaServiceRequestRecord) : String = {

    if(ksrr.communityArea == null) {
      return "Total - No community area for record " + ksrr.srNumber + "... skipping update to community tables"
    } else {
      val incTotal = new Increment(Bytes.toBytes(ksrr.communityArea))
      incTotal.addColumn(Bytes.toBytes("totals"), Bytes.toBytes("calls"), 1)
      totalServiceCalls.increment(incTotal)
      val incType = new Increment(Bytes.toBytes(ksrr.communityArea + ":" + ksrr.srType))
      incType.addColumn(Bytes.toBytes("calls"), Bytes.toBytes("calls"), 1)
      serviceCallsByComm.increment(incType)
    }
    "Total - Updated speed layer for new " + ksrr.srType + " | service request " + ksrr.srNumber + " for " + ksrr.communityArea
  }

  def incrementOpenCalls(ksrr : KafkaServiceRequestRecord) : String = {

    val inc = new Increment(Bytes.toBytes(ksrr.ownerDepartment))
    if(ksrr.status == "Open") {
      inc.addColumn(Bytes.toBytes("calls"), Bytes.toBytes("calls"), 1)
      openServiceCalls.increment(inc)
    } else {
      return "Open - New service request is already closed. Nothing to increment."
    }
    "Open - Updated speed layer for new service request " + ksrr.srNumber + " in " + ksrr.ownerDepartment
  }

  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println(s"""
        |Usage: StreamFlights <brokers> 
        |  <brokers> is a list of one or more Kafka brokers
        | 
        """.stripMargin)
      System.exit(1)
    }
    
    val Array(brokers) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("StreamServiceRequests")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    // Create direct kafka stream with brokers and topics
    val topicsSet = Set("mtrichardson-311-calls")
    // Create direct kafka stream with brokers and topics
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc, PreferConsistent,
      Subscribe[String, String](topicsSet, kafkaParams)
    )

    // Get the lines, split them into words, count the words and print
    val serializedRecords = stream.map(_.value);

    val ksrr = serializedRecords.map(rec => mapper.readValue(rec, classOf[KafkaServiceRequestRecord]))

    // Update speed table    
    val processedTotalRequests = ksrr.map(incrementTotalCalls)
    val processedOpenRequests = ksrr.map(incrementOpenCalls)
    val processedDeltas = ksrr.map(incrementDeltas)
    processedTotalRequests.print()
    processedOpenRequests.print()
    processedDeltas.print()
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}
