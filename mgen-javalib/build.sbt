
// Remember, sbt needs empty lines between active settings

name := "mgen-javalib"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

crossPaths := false

retrieveManaged := true

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false
