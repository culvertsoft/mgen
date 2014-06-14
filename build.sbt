
// Remember, sbt needs empty lines between active settings

name := "mgen"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

crossPaths := false

retrieveManaged := true

lazy val mgen_api = project in file("mgen-api")

lazy val mgen_compiler = (project in file("mgen-compiler")).dependsOn(mgen_api)

lazy val mgen_javalib = (project in file("mgen-javalib")).dependsOn(mgen_api)

lazy val mgen_javagenerator = (project in file("mgen-javagenerator")).dependsOn(mgen_api, mgen_compiler)

// lazy val mgen_cpplib = project in file("mgen-cpplib")

lazy val mgen_cppgenerator = (project in file("mgen-cppgenerator")).dependsOn(mgen_api, mgen_compiler)

// lazy val mgen_javascriptlib = project in file("mgen-javascriptlib")

lazy val mgen_javascriptgenerator = (project in file("mgen-javascriptgenerator")).dependsOn(mgen_api, mgen_compiler)

lazy val mgen_visualdesigner = (project in file("mgen-visualdesigner")).dependsOn(mgen_api, mgen_compiler, mgen_javalib, mgen_javagenerator)
