// Settings
// Remember, sbt needs empty lines between active settings
// name := "hello"
// version := "SNAPSHOT"
// scalaVersion := "2.10.4"

crossPaths := false

retrieveManaged := true

unmanagedSourceDirectories in Compile += baseDirectory.value / "src_generated"

libraryDependencies += "default" % "mgen-api" % "0.1-SNAPSHOT"

libraryDependencies += "default" % "mgen-compiler" % "0.1-SNAPSHOT"

libraryDependencies += "default" % "mgen-javalib" % "0.1-SNAPSHOT"

libraryDependencies += "default" % "mgen-javagenerator" % "0.1-SNAPSHOT"

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

// libraryDependencies += "com.miglayout" % "miglayout" % "4.2"

