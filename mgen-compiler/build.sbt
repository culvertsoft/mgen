// Settings
// Remember, sbt needs empty lines between active settings
// name := "hello"
// version := "SNAPSHOT"
// scalaVersion := "2.10.4"

crossPaths := false

retrieveManaged := true

libraryDependencies += "default" % "mgen-api" % "0.1-SNAPSHOT"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

