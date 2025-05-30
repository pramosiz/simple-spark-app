package constants

object Queries {

  //    IF(role = 'Senior', "T2", "T1") as team
  // Cannot do column operations with new columns in the same select
  val gatherQuery: String = """
       SELECT /*+ COALESCE_NUM_PARTITIONS_PLACEHOLDER */
          firstName,
          age,
          IF(age > 40, 'Senior', 'Junior') AS role,
          CONCAT_WS('-',
            firstName,
            IF(children = 0, 'no', 'yes'),
            IF(salary > 15000, 'high', 'low'))
          AS status,
          timestamp,
          processMonth
       FROM people
    """

  val timestampFilter: String = """
       SELECT firstName, age, role, status, timestamp, processMonth
       FROM processed_people
       WHERE timestamp > current_timestamp() - interval 10 day
    """

  val filterDays1: String = """
       WITH dates AS (
          SELECT
              CAST(YEAR(CURRENT_DATE) AS INT) * 100 + CAST(MONTH(CURRENT_DATE) AS INT) AS month,
              CAST(DAYOFMONTH(CURRENT_DATE) AS INT) AS day
          UNION ALL
          SELECT
              CAST(YEAR(DATE_SUB(CURRENT_DATE, 1)) AS INT) * 100 + CAST(MONTH(DATE_SUB(CURRENT_DATE, 1)) AS INT),
              CAST(DAYOFMONTH(DATE_SUB(CURRENT_DATE, 1)) AS INT)
      )
      SELECT processed_people.firstName, processed_people.processMonth FROM processed_people, dates
      WHERE processed_people.processMonth = dates.month
    """

  val filterDays2: String = """
        WITH dates AS (
          SELECT
              CAST(YEAR(CURRENT_DATE) AS INT) * 100 + CAST(MONTH(CURRENT_DATE) AS INT) AS month,
              CAST(DAYOFMONTH(CURRENT_DATE) AS INT) AS day
          UNION ALL
          SELECT
              CAST(YEAR(DATE_SUB(CURRENT_DATE, 1)) AS INT) * 100 + CAST(MONTH(DATE_SUB(CURRENT_DATE, 1)) AS INT),
              CAST(DAYOFMONTH(DATE_SUB(CURRENT_DATE, 1)) AS INT)
      )
      SELECT month, day FROM dates
    """
}
