
// Remember, sbt needs empty lines between active settings

name := "mgen-cppgenerator"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

retrieveManaged := true

libraryDependencies += "se.culvertsoft" % "mgen-compiler" % version.value

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false
