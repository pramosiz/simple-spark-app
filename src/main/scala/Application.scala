import constants.Queries._
import org.apache.log4j.Logger
import org.apache.spark.sql.functions.{avg, col, count, sum}
import org.apache.spark.sql.{DataFrame, SparkSession, functions}
import util.ServiceImpl._

object Application {

  private val logger: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {

    try {
      logger.info("--------------------------------------------")
      logger.info("------ Starting the Spark Application ------")
      logger.info("--------------------------------------------")

      // Inicializa SparkSession
      val sparkSession: SparkSession = initSparkSession

      // Carga el archivo JSON en el contexto de Spark
      val filePathOnExecutor: String = "/opt/bitnami/spark/data/shared/data.json"

      // Lee el archivo JSON
      val dfInit: DataFrame = readJson(sparkSession, filePathOnExecutor)
        .repartition(1) // número de particiones a 4
      //    val dfInit: DataFrame = readJson(sparkSession, "./src/main/resources/data_aux.json")

      // Cuenta las particiones
      logger.info(s"Number of partitions at init: ${dfInit.rdd.getNumPartitions}")

      // Muestra la estructura inicial del DataFrame
      logger.info("Initial DataFrame:")
      dfInit.printSchema
      dfInit.show

      // Aplana (flat) el DataFrame
      logger.info("Flattened DataFrame:")
      val flattenedDF = plainJson(dfInit)
      flattenedDF.show
      flattenedDF.createOrReplaceTempView("people")

      //  Define la consulta SQL
      val minusDays: String = "25"
      val df1: DataFrame = lpadFunction(flattenedDF, minusDays.toInt)
      logger.info("DataFrame with processDate:")
      df1.show

      // Primera agrupación
      val gatherQueryWithCoalesce: String = getQueryAppliedCoalesceHint(gatherQuery, 2)
      val df2: DataFrame = sparkSession.sql(gatherQueryWithCoalesce)
      logger.info("DataFrame after query 'gatherQuery':")
      df2.show
      df2.createOrReplaceTempView("processed_people")

      // Comprobar el número de particiones
      logger.info(s"Number of partitions after query 'gatherQuery': ${df2.rdd.getNumPartitions}")

      // Filtro timestamp
      val df3: DataFrame = sparkSession.sql(timestampFilter)
      logger.info("Filtered DataFrame:")
      df3.show

      // Comprobar el número de particiones
      logger.info(s"Number of partitions after query 'timestampFilter': ${df3.rdd.getNumPartitions}")

      // Filtro 1 con días
      val dfDays: DataFrame = sparkSession.sql(filterDays1)
      logger.info("DataFrame with days:")
      dfDays.show

      // Comprobar el número de particiones
      logger.info(s"Number of partitions after query 'filterDays1': ${dfDays.rdd.getNumPartitions}")

      // Filtro 2 con días
      val dfDays2: DataFrame = sparkSession.sql(filterDays2)
      logger.info("DataFrame with days:")
      dfDays2.show

      // Comprobar el número de particiones
      logger.info(s"Number of partitions after query 'filterDays2': ${dfDays2.rdd.getNumPartitions}")

      // JOB 1: Agrupación por grupos
      logger.info("Starting JOB 1: Grouping by groups")
      val job1 = flattenedDF.groupBy("children")
        .agg(
          avg("age").as("edadPromedio"),
          sum("salary").as("salarioTotal"),
          count("*").as("totalPersonas")
        ).cache

      job1.count
      logger.info("Job 1 completed: Grouping by groups")
      job1.show

      // JOB 2: Filtering and sorting
      logger.info("Starting JOB 2: Filtering and sorting")
      val job2 = flattenedDF.filter(functions.col("salary") > 10000)
        .orderBy(col("age").desc)
        .select("firstName", "age", "salary")

      val result2 = job2.collect
      logger.info(s"Job 2 completed - Found ${result2.length} records")
      job2.show

      // Sleep 10 minutes for studying Spark UI
//      logger.info("Sleeping for 10 minutes to explore the Spark UI...")
//      Thread.sleep(10 * 60 * 1000) // 10 minutes
//      logger.info("Resuming execution after sleep")
//
//      // JOB 3: Analysis with windows
//      logger.info("Starting JOB 3: Analysis with windows")
//      import org.apache.spark.sql.expressions.Window
//      val windowSpec = Window.partitionBy("processMonth").orderBy("salary")
//      val job3 = flattenedDF.withColumn("rankSalarial", functions.rank().over(windowSpec))
//        .withColumn("avgSalary", functions.avg("salary").over(windowSpec))
//      job3.write.mode("overwrite")
//        .parquet("/opt/bitnami/spark/data/shared/job3_output")
//      logger.info("Job 3 completed - Output written to /opt/bitnami/spark/data/shared/job3_output")
//      job3.show

      // Fin del programa
      logger.info("End of the program")
      sparkSession.stop

    } catch {
      case e: Exception =>
        logger.error("An error occurred during the execution of the program", e)
        System.exit(1)
    }

  }
}