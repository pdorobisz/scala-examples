name := "Cats Effect examples"

version := "1.0"

organization := "pdorobisz"

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.4.5" withSources() withJavadoc()
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps"
)
