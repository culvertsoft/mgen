
// Remember, sbt needs empty lines between active settings

name := "mgen-javalib"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

retrieveManaged := true

unmanagedSourceDirectories in Test += baseDirectory.value / "src_generated/test/java"

autoScalaLibrary := false

compileOrder := CompileOrder.JavaThenScala

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value % Test

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % Test

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

publishArtifact in (Compile, packageDoc) := false
