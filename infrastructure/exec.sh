#!/bin/bash

echo "Starting Spark Application..."
# Definimos la ruta de los JARs
JARS_DIR=/opt/bitnami/spark/jars

sleep 20

spark-submit \
 --master spark://spark-master:7077 \
 --conf spark.jars.ivy=$JARS_DIR \
 --conf spark.driver.extraJavaOptions="-Dlog4j2.configurationFile=file:/opt/bitnami/spark/conf/log4j2.properties" \
 --conf spark.executor.extraJavaOptions="-Dlog4j2.configurationFile=file:/opt/bitnami/spark/conf/log4j2.properties" \
 --conf spark.eventLog.enabled=true \
 --conf spark.eventLog.dir=file:/opt/bitnami/spark/spark-events \
 --conf spark.io.compression.codec=snappy \
 --jars $JARS_DIR/spark-core_2.12-3.5.5.jar,$JARS_DIR/spark-sql_2.12-3.5.5.jar \
 --class Application \
 /opt/bitnami/spark/app/app.jar

# 1. Documentación de spark-submit -> https://spark.apache.org/docs/latest/configuration.html
# 2. Documentación para spark.hadoop -> https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SecureMode.html

# 3. Se soluciona el problema que no encuentra la ruta de las librerías ".ivy2/local" --conf spark.jars.ivy=$JARS_DIR \  

# arg1 arg2 - Para pasar argumentos a la aplicación
#  --jars $JARS_DIR/spark-core_2.12-3.5.5.jar,$JARS_DIR/spark-sql_2.12-3.5.5.jar $JARS_DIR/kafka-clients-2.6.0.jar,$JARS_DIR/spark-sql-kafka-0-10_2.12-3.5.5.jar \
#--conf spark.files=$SHARED_DIR/data.json \