ThisBuild / version := "1.0.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.19"

val sparkVersion = "3.5.5"
val scalaTestVersion = "3.2.9"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  // "org.apache.spark" %% "spark-sql-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  // "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
  "app.jar"
}

Test / fork := true
Test / javaOptions ++= Seq(
  "--add-opens=java.base/java.nio=ALL-UNNAMED",
  "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
)

lazy val root = (project in file("."))
  .settings(
    name := "SimpleSparkApp"
)
