
// Remember, sbt needs empty lines between active settings

name := "mgen-jsonschemaparser"

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

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % Test

libraryDependencies += "se.culvertsoft" % "mgen-api" % version.value

libraryDependencies += "org.jsonschema2pojo" % "jsonschema2pojo-core" % "0.4.5"

libraryDependencies += "org.jsonschema2pojo" % "jsonschema2pojo-cli" % "0.4.5"

EclipseKeys.withSource := true

EclipseKeys.relativizeLibs := false
