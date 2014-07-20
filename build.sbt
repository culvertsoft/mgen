
// Remember, sbt needs empty lines between active settings

name := "mgen"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

retrieveManaged := true

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")

javacOptions ++= Seq("-Werror")

lazy val mgen_api = project in file("mgen-api")

lazy val mgen_compiler = (project in file("mgen-compiler")).dependsOn(mgen_api)

lazy val mgen_javalib = (project in file("mgen-javalib")).dependsOn(mgen_api)

lazy val mgen_javagenerator = (project in file("mgen-javagenerator")).dependsOn(mgen_api, mgen_compiler)

lazy val mgen_cpplib = project in file("mgen-cpplib")

lazy val mgen_cppgenerator = (project in file("mgen-cppgenerator")).dependsOn(mgen_api, mgen_compiler)

lazy val mgen_javascriptlib = project in file("mgen-javascriptlib")

lazy val mgen_javascriptgenerator = (project in file("mgen-javascriptgenerator")).dependsOn(mgen_api, mgen_compiler)

lazy val mgen_visualdesigner = (project in file("mgen-visualdesigner")).dependsOn(mgen_api, mgen_compiler, mgen_javalib, mgen_javagenerator)

lazy val mgen_javacheck_depends = (project in file("mgen-integrationtests/javacheck/depends")).dependsOn(mgen_api, mgen_javalib)

lazy val mgen_javacheck_write = (project in file("mgen-integrationtests/javacheck/write")).dependsOn(mgen_api, mgen_javalib)

lazy val mgen_javacheck_read = (project in file("mgen-integrationtests/javacheck/read")).dependsOn(mgen_api, mgen_javalib)

lazy val mgen_javascriptcheck_depends = (project in file("mgen-integrationtests/javascriptcheck/depends")).dependsOn(mgen_api, mgen_javascriptlib)

lazy val mgen_javascriptcheck_write = (project in file("mgen-integrationtests/javascriptcheck/write")).dependsOn(mgen_api, mgen_javascriptlib)

lazy val mgen_javascriptcheck_read = (project in file("mgen-integrationtests/javascriptcheck/read")).dependsOn(mgen_api, mgen_javascriptlib)
