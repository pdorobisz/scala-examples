name := "Akka examples"

version := "1.0"

organization := "pdorobisz"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.14",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.14" % Test,

  "com.typesafe.akka" %% "akka-stream" % "2.5.14",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.14" % Test,

  "com.typesafe.akka" %% "akka-http" % "10.1.3",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % Test
)
