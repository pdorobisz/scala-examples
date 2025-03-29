name := "Cats Effect examples"

version := "1.0"

organization := "pdorobisz"

scalaVersion := "3.6.4"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.13.0",
  "org.typelevel" %% "cats-effect" % "3.5.7" withSources() withJavadoc(),
  "org.typelevel" %% "cats-mtl" % "1.4.0"
)
