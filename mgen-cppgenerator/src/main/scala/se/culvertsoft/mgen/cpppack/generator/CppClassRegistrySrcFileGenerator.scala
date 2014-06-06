package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.JavaConverters.mapAsScalaMapConverter

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.types.Module

object CppClassRegistrySrcFileGenerator extends CppClassRegistryGenerator(".cpp") {

   override def mkIncludes(
      referencedModules: Seq[Module],
      generatorSettings: java.util.Map[String, String]) {

      CppGenUtils.include("ClassRegistry.h")

      val mkUnityBuild = generatorSettings.asScala.getOrElse("generate_unity_build", throw new GenerationException("Missing <generate_unity_build> setting for C++ generator")).toBoolean

      if (mkUnityBuild) {
         for (referencedModule <- referencedModules)
            for (t <- referencedModule.types().values())
               CppGenUtils.include(t, ".cpp")
      }

      txtBuffer.endl()

   }

   override def mkDefaultCtor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
      txtBuffer.tabs(0).textln(s"ClassRegistry::ClassRegistry() {")
      for (m <- referencedModules)
         for (t <- m.types().values())
            txtBuffer.tabs(1).textln(s"add<${t.fullName().replaceAllLiterally(".", "::")}>();")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
   }

   override def mkDestructor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
      txtBuffer.tabs(0).textln(s"ClassRegistry::~ClassRegistry() {")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
   }

}