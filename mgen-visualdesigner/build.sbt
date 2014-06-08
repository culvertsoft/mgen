// Settings
// Remember, sbt needs empty lines between active settings
// name := "hello"
// version := "SNAPSHOT"
// scalaVersion := "2.10.4"

version := System.getenv("MGEN_BUILD_VERSION")

crossPaths := false

retrieveManaged := true

unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated/main/java"

libraryDependencies += "default" % "mgen-api" % version.value

libraryDependencies += "default" % "mgen-compiler" % version.value

libraryDependencies += "default" % "mgen-javalib" % version.value

libraryDependencies += "default" % "mgen-javagenerator" % version.value

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

// libraryDependencies += "com.miglayout" % "miglayout" % "4.2"

