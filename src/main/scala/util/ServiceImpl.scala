package util

import org.apache.log4j.Logger
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

object ServiceImpl {

  private val logger: Logger = Logger.getLogger(getClass.getName)

  def initSparkSession: SparkSession =
    SparkSession.builder()
      .appName("Simple Spark Application")
      //      .master("local[*]")
      .getOrCreate()

  def readJson(sparkSession: SparkSession, filePath: String): DataFrame = {
    logger.info(s"Reading JSON file from path: $filePath")
    sparkSession.read
      .option("multiline", "true") // IMPORTANT
      .json(filePath)
    //      .repartition(1)
  }

  def plainJson(df: DataFrame): DataFrame = {
    df.select(explode(col("data")).alias("person")) // Explota la columna 'data' en filas
      .select(
        col("person.firstName").alias("firstName"),
        col("person.age").alias("age"),
        col("person.children").alias("children"),
        col("person.salary").alias("salary"),
        col("person.timestamp").alias("timestamp"),
        col("person.processMonth").alias("processMonth"),
        col("person.processDay").alias("processDay")
      )
  }

  def lpadFunction(df: DataFrame, minusDays: Int): DataFrame = {
    df.withColumn(
        "processDate",
        to_date(concat(col("processMonth"), lpad(col("processDay").cast("string"), 2, "0")), "yyyyMMdd")
      )
      .filter(
        to_date(concat(col("processMonth"), lpad(col("processDay").cast("string"), 2, "0")), "yyyyMMdd")
          .between(current_date() - minusDays, current_date()))
  }

  def getQueryAppliedCoalesceHint(query: String, numPartitions: Int): String = {
    query.replace("/*+ COALESCE_NUM_PARTITIONS_PLACEHOLDER */", s"/*+ COALESCE($numPartitions) */")
  }
}
