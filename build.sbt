
// Remember, sbt needs empty lines between active settings

name := "mgen"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

javacOptions ++= Seq("-Werror")

lazy val mgen_api = project in file("mgen-api")

lazy val mgen_idlparser = (project in file("mgen-idlparser")).dependsOn(mgen_api)

lazy val mgen_jsonschemaparser = (project in file("mgen-jsonschemaparser")).dependsOn(mgen_api)

lazy val mgen_protobufparser = (project in file("mgen-protobufparser")).dependsOn(mgen_api)

lazy val mgen_xmlschemaparser = (project in file("mgen-xmlschemaparser")).dependsOn(mgen_api)

lazy val mgen_idlgenerator = (project in file("mgen-idlgenerator")).dependsOn(mgen_api)

lazy val mgen_javalib = (project in file("mgen-javalib")).dependsOn(mgen_api)

lazy val mgen_compiler = (project in file("mgen-compiler")).dependsOn(mgen_idlparser, mgen_idlgenerator)

lazy val mgen_javagenerator = (project in file("mgen-javagenerator")).dependsOn(mgen_compiler)

lazy val mgen_pythongenerator = (project in file("mgen-pythongenerator")).dependsOn(mgen_compiler)

lazy val mgen_cppgenerator = (project in file("mgen-cppgenerator")).dependsOn(mgen_compiler)

lazy val mgen_javascriptgenerator = (project in file("mgen-javascriptgenerator")).dependsOn(mgen_compiler)

lazy val mgen_visualdesigner = (project in file("mgen-visualdesigner")).dependsOn(mgen_compiler, mgen_javalib)

// lazy val mgen_javacheck_depends = (project in file("mgen-integrationtests/javacheck/depends")).dependsOn(mgen_javalib)

// lazy val mgen_javacheck_write = (project in file("mgen-integrationtests/javacheck/write")).dependsOn(mgen_javalib)

// lazy val mgen_javacheck_read = (project in file("mgen-integrationtests/javacheck/read")).dependsOn(mgen_javalib)
