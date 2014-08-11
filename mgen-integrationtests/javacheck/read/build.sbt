
// Remember, sbt needs empty lines between active settings

name := "mgen-javacheck-read"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

unmanagedSourceDirectories in Test += baseDirectory.value / "src_generated/test/java"

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-javalib" % version.value

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

publishArtifact in (Compile, packageDoc) := false
