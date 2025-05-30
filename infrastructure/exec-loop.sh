#!/bin/bash

echo "Starting Spark Application..."

# Definimos la ruta de los JARs
JARS_DIR=/opt/bitnami/spark/jars

while true; do
    spark-submit \
     --master spark://spark-master:7077 \
     --conf spark.jars.ivy=$JARS_DIR \
     --conf spark.driver.extraJavaOptions="-Dlog4j2.configurationFile=file:/opt/bitnami/spark/conf/log4j2.properties" \
     --conf spark.executor.extraJavaOptions="-Dlog4j2.configurationFile=file:/opt/bitnami/spark/conf/log4j2.properties" \
     --jars $JARS_DIR/spark-core_2.12-3.5.5.jar,$JARS_DIR/spark-sql_2.12-3.5.5.jar \
     --class Application \
     /opt/bitnami/spark/app/app.jar
    sleep 180
done
