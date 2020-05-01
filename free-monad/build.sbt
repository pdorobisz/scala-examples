name := "Free monad examples"

version := "1.0"

organization := "pdorobisz"

scalaVersion := "2.13.2"

scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-free" % "2.1.1"
)
