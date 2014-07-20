name := "mgen-javascriptcheck-depends"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

retrieveManaged := true

unmanagedSourceDirectories in Test += baseDirectory.value / "src_generated/test/javascript"

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

publishArtifact in (Compile, packageDoc) := false