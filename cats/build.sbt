name := "Cats examples"

version := "1.0"

organization := "pdorobisz"

scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "2.1.1",
  "org.typelevel" %% "cats-effect" % "2.1.1"
)

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)
