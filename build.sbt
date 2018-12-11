scalaVersion := "2.12.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "utf-8",
  "-explaintypes",
  "-feature",
  "-Ywarn-unused:imports",
  "-Ywarn-infer-any",
  "-Ypartial-unification",
  "-language:higherKinds"
)

val confluentAvroVersion = "5.0.0"
val kafkaVersion = "2.0.0"
val avro4sVersion = "2.0.3"

resolvers ++= Seq(
  "confluent-release" at "https://packages.confluent.io/maven/"
)

libraryDependencies ++=
  Seq(
    "org.apache.kafka" % "kafka-clients" % kafkaVersion,
    "io.confluent" % "kafka-avro-serializer" % confluentAvroVersion,
    "com.sksamuel.avro4s" %% "avro4s-core" % avro4sVersion
  )
