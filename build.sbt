scalaVersion := "2.12.7"

organization in ThisBuild := "io.github.moleike"


val compilerOptions = Seq(
  "-deprecation",
  "-encoding", "utf-8",
  "-explaintypes",
  "-feature",
  "-unchecked",
  "-Ywarn-unused:imports",
  "-Ywarn-infer-any",
  "-Ypartial-unification",
  "-language:higherKinds"
)

val confluentAvroVersion = "5.0.0"
val kafkaVersion = "2.0.0"
val avro4sVersion = "2.0.3"
val scalaCheckVersion = "1.13.5"
val scalaTestVersion = "3.0.5"

resolvers ++= Seq(
  "confluent-release" at "https://packages.confluent.io/maven/"
)

lazy val baseSettings = Seq(
  scalacOptions ++= compilerOptions,
  scalacOptions in (Compile, console) ~= {
    _.filter(Set("-Ywarn-unused-import"))
  }
)

lazy val allSettings = baseSettings ++ publishSettings

lazy val root = project.in(file("."))
  .settings(allSettings)
  .settings(moduleName := "kafka-streams-avro-scala")
  .settings(
    libraryDependencies ++= Seq(
      "org.apache.kafka" % "kafka-clients" % kafkaVersion,
      "io.confluent" % "kafka-avro-serializer" % confluentAvroVersion,
      "com.sksamuel.avro4s" %% "avro4s-core" % avro4sVersion,
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test,
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    )
  )
  .settings(
    initialCommands in console :=
      """
        |import org.apache.kafka.common.serialization._
        |import io.confluent.kafka.serializers._
        |import com.sksamuel.avro4s._
      """.stripMargin
  )

lazy val publishSettings = Seq(
  useGpg := false,
  homepage := Some(url("https://github.com/moleike/kafka-streams-avro-scala")),
  licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  publishMavenStyle := true,
  publishArtifact in Test := true,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/moleike/kafka-streams-avro-scala"),
      "scm:git:git@github.com:moleike/kafka-streams-avro-scala.git"
    )
  ),
  pomExtra := (
    <developers>
      <developer>
        <id>moleike</id>
        <name>Alexandre Moreno</name>
        <url>https://twitter.com/moleike</url>
      </developer>
    </developers>
  )
)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

