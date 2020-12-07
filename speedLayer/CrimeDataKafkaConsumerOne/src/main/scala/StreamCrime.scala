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

object StreamCrime {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  val hbaseConf: Configuration = HBaseConfiguration.create()
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  hbaseConf.set("hbase.zookeeper.quorum", "localhost")
  
  val hbaseConnection = ConnectionFactory.createConnection(hbaseConf)
  val latestCrimes = hbaseConnection.getTable(TableName.valueOf("mtrichardson_crime_calls_by_comm_area"))
  val latestCrimesByType = hbaseConnection.getTable(TableName.valueOf("mtrichardson_crime_by_comm"))

  def getLatestCrimes(kcr : KafkaCrimeRecord) : String = {
      if(kcr.communityArea == "null") {
        "No community area for record " + kcr.id + "... skipping update to community tables"
      } else {
        val inc = new Increment(Bytes.toBytes(kcr.communityArea))
        inc.addColumn(Bytes.toBytes("totals"), Bytes.toBytes("crimes"), 1)
        latestCrimes.increment(inc)
        val incDos = new Increment(Bytes.toBytes(kcr.communityArea + ":" + kcr.primaryType))
        incDos.addColumn(Bytes.toBytes("crimes"), Bytes.toBytes("crimes"), 1)
        latestCrimesByType.increment(incDos)
        "Successful insert to Crime table: " + kcr.id + ", " + kcr.communityArea + " - " + kcr.primaryType
      }

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
    val sparkConf = new SparkConf().setAppName("StreamCrime")
    val ssc = new StreamingContext(sparkConf, Seconds(10))

    // Create direct kafka stream with brokers and topics
    val topicsSet = Set("mtrichardson-crime-api")
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

    val kcrs = serializedRecords.map(rec => mapper.readValue(rec, classOf[KafkaCrimeRecord]))

    // Update speed table    
    val processedFlights = kcrs.map(getLatestCrimes)
    processedFlights.print()
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}
