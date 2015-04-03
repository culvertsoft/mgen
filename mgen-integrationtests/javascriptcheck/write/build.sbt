name := "mgen-javascriptcheck-write"

organization := "se.culvertsoft"

version := scala.util.Properties.envOrElse("MGEN_BUILD_VERSION", "SNAPSHOT")

scalaVersion := "2.11.6"

isSnapshot := version.value.contains("SNAPSHOT")

// TESTS USING JASMINE

seq(jasmineSettings : _*)

appJsDir <+= baseDirectory { x => x }

appJsLibDir <+= sourceDirectory { x => x / "test" / "javascript" }

jasmineTestDir <+= sourceDirectory { x => x / "test" / "javascript" }

jasmineRequireJsFile <+= sourceDirectory { x => x / "test" / "javascript" / "require-2.0.6.js" }

jasmineConfFile <+= sourceDirectory { x => x / "test" / "javascript" / "test.dependencies.js" }

jasmineRequireConfFile <+= sourceDirectory { x => x / "test" / "javascript" / "require.conf.js" }

(test in Test) <<= test in Test dependsOn (jasmine)
