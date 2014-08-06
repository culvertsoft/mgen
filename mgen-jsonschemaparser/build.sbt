
// Remember, sbt needs empty lines between active settings

name := "mgen-jsonschemaparser"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "org.jsonschema2pojo" % "jsonschema2pojo-core" % "0.4.5"

libraryDependencies += "org.jsonschema2pojo" % "jsonschema2pojo-cli" % "0.4.5"

retrieveManaged := true

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

publishArtifact in (Compile, packageDoc) := false
