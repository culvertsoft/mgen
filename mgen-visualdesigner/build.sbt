
// Remember, sbt needs empty lines between active settings

import sbt._
import Process._
import Process.stringToProcess
import Keys._

lazy val generateCodeTask = taskKey[Unit]("Generate the data model for mgen-visualdesigner.")

generateCodeTask := { 
  println("Generating the data model code for mgen-visualdesigner")
  val compiler = s"../mgen-compiler/target/mgen-compiler-assembly-${version.value}.jar"
  val args = "-project=\"model/project.xml\" -plugin_paths=\"/home/johan/git/mgen/mgen-javagenerator/target\""
  val command = s"java -jar $compiler $args"
  command !
} 

compile in Compile := {
  generateCodeTask.value;
  (compile in Compile).value
}

name := "mgen-visualdesigner"

organization := "se.culvertsoft"

EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

crossPaths := false

retrieveManaged := true

unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated/main/java"

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-compiler" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-javalib" % version.value

libraryDependencies += "se.culvertsoft" % "mgen-javagenerator" % version.value

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

// libraryDependencies += "com.miglayout" % "miglayout" % "4.2"

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false
