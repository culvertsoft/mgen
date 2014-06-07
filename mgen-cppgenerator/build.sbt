// Settings
// Remember, sbt needs empty lines between active settings
// name := "hello"
// version := "SNAPSHOT"
// scalaVersion := "2.10.4"

version := System.getenv("MGEN_BUILD_VERSION")

crossPaths := false

retrieveManaged := true

libraryDependencies += "default" % "mgen-api" % version.value

libraryDependencies += "default" % "mgen-compiler" % version.value

