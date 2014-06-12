// Settings
// Remember, sbt needs empty lines between active settings
// name := "hello"
// version := "SNAPSHOT"
// scalaVersion := "2.10.4"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

crossPaths := false

retrieveManaged := true

libraryDependencies += "default" % "mgen-api" % version.value

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false