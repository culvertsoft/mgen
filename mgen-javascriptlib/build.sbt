
// Remember, sbt needs empty lines between active settings

name := "mgen-javascriptlib"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

isSnapshot := version.value.contains("SNAPSHOT")

crossPaths := false

retrieveManaged := true

seq(jsSettings : _*)

(compile in Compile) <<= compile in Compile dependsOn (JsKeys.js in Compile)

(JsKeys.strictMode in (Compile)) := true

(JsKeys.prettyPrint in (Compile)) := true

(resourceManaged in (Compile, JsKeys.js)) <<= (baseDirectory in Compile)(_ / "target")

// TESTS USING JASMINE

seq(jasmineSettings : _*)

appJsDir <+= sourceDirectory { src => src / "main" / "javascript" }

appJsLibDir <+= sourceDirectory { src => src / "main" / "javascript" }

jasmineTestDir <+= sourceDirectory { src => src / "test" / "javascript" }

jasmineRequireJsFile <+= sourceDirectory { src => src / "test" / "javascript" / "require-2.0.6.js" }

jasmineConfFile <+= sourceDirectory { src => src / "test" / "javascript" / "test.dependencies.js" }

jasmineRequireConfFile <+= sourceDirectory { src => src / "test" / "javascript" / "require.conf.js" }

(test in Test) <<= test in Test dependsOn (jasmine)