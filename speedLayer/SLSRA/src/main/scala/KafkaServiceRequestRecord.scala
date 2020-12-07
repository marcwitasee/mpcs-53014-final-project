import scala.reflect.runtime.universe._


case class KafkaServiceRequestRecord(
    ownerDepartment: String,
    createdDate: String,
    closedDate: String,
    status: String,
    srNumber: String,
    communityArea: String,
    srType: String)