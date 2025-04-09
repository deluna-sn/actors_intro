ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

lazy val root = (project in file("."))
  .settings(
    name := "actorsintro",
  )

libraryDependencies +=   "org.apache.pekko" %% "pekko-actor-typed" % "1.1.3"