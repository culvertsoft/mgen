
// Remember, sbt needs empty lines between active settings

name := "mgen-javagenerator"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

crossPaths := false

retrieveManaged := true

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-compiler" % version.value

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false
