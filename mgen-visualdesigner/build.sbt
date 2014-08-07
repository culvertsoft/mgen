
// Remember, sbt needs empty lines between active settings

name := "mgen-visualdesigner"

organization := "se.culvertsoft"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

retrieveManaged := true

unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated/main/java"

libraryDependencies += "se.culvertsoft" % "mgen-compiler" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-javalib" % version.value

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += "com.miglayout" % "miglayout-swing" % "4.2"

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

