
// Remember, sbt needs empty lines between active settings

name := "mgen-api"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

pomIncludeRepository := { _ => false }

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomExtra := (
  <url>http://github.com/culvertsoft/mgen</url>
  <licenses>
    <license>
      <name>GPL 2</name>
      <url>https://github.com/culvertsoft/mgen/blob/master/LICENSE</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:culvertsoft/mgen.git</url>
    <connection>scm:git:git@github.com:culvertsoft/mgen.git</connection>
  </scm>
  <developers>
    <developer>
      <id>PhroZenOne</id>
      <name>Mikael Berglund</name>
    </developer>
    <developer>
      <id>GiGurra</id>
      <name>Johan Kjölhede</name>
    </developer>
  </developers>)

crossPaths := false

autoScalaLibrary := false

EclipseKeys.projectFlavor := EclipseProjectFlavor.Java

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1"

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false

publishArtifact in (Compile, packageDoc) := false
