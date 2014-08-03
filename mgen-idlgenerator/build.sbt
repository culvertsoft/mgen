
// Remember, sbt needs empty lines between active settings

name := "mgen-idlgenerator"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-compiler" % version.value

retrieveManaged := true

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

publishArtifact in (Compile, packageDoc) := false
