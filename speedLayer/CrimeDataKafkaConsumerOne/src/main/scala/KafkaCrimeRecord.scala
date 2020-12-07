import scala.reflect.runtime.universe._


case class KafkaCrimeRecord(
    id: String,
    date: String,
    primaryType: String,
    arrest: String,
    communityArea: String,
    updatedOn: String)