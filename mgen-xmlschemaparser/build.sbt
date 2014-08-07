
// Remember, sbt needs empty lines between active settings

name := "mgen-xmlschemaparser"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "com.sun.xml.bind" % "jaxb-impl" % "2.2.7"

libraryDependencies += "com.sun.xml.bind" % "jaxb-xjc" % "2.2.7"

retrieveManaged := true

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

publishArtifact in (Compile, packageDoc) := false
