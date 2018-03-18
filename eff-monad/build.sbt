name := "Eff Monad"

version := "1.0"

organization := "pdorobisz"

scalaVersion := "2.12.4"

libraryDependencies += "org.atnos" %% "eff" % "5.1.0"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")

scalacOptions += "-Ypartial-unification"
