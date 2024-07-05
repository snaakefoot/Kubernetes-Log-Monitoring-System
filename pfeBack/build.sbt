scalaVersion    := "2.13.4"
enablePlugins(JavaAppPackaging)
name := "pfeBack"

lazy val akkaHttpVersion = "10.2.7"
lazy val akkaVersion     = "2.6.18"
lazy val circeVersion    = "0.14.1"

lazy val akkaHttpJsonSerializersVersion = "1.39.2"
resolvers += "Akka library repository".at("https://repo.akka.io/maven")
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"                  % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % "2.6.18",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % "2.6.18" % Test,
  "com.typesafe.akka" %% "akka-persistence-testkit" % akkaVersion % Test,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "ch.megard" %% "akka-http-cors" % "1.1.3",
  "com.datastax.spark" % "spark-cassandra-connector_2.12" % "3.2.0",
  "joda-time" % "joda-time" % "2.9.3",
  "org.joda" % "joda-convert" % "1.8",
  "org.scala-lang.modules" %% "scala-collection-compat" % "2.5.0",
  // ScalaTest for unit testing
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed"     % akkaVersion,
  "com.datastax.oss"  %  "java-driver-core"           % "4.13.0",   // See https://github.com/akka/alpakka/issues/2556
  "com.typesafe.akka" %% "akka-persistence-cassandra" % "1.0.5",
  "io.circe"          %% "circe-core"                 % circeVersion,
  "io.circe"          %% "circe-generic"              % circeVersion,
  "io.circe"          %% "circe-parser"               % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe"            % akkaHttpJsonSerializersVersion,
  "de.heikoseeberger" %% "akka-http-jackson"          % akkaHttpJsonSerializersVersion,
  "ch.qos.logback"    % "logback-classic"             % "1.2.10",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.github.jwt-scala" %% "jwt-circe" % "10.0.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.18",
  "org.apache.kafka" %% "kafka" % "3.7.0",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "org.slf4j" % "slf4j-api" % "1.7.32",
  "ch.qos.logback" % "logback-classic" % "1.2.6",
  "com.typesafe.akka" %% "akka-cluster-sharding-typed" % akkaVersion,
  "com.lightbend.akka" %% "akka-projection-core" % "1.2.4",
  "com.lightbend.akka" %% "akka-projection-eventsourced" % "1.2.4",
  "com.lightbend.akka" %% "akka-projection-cassandra" % "1.2.4",

)
