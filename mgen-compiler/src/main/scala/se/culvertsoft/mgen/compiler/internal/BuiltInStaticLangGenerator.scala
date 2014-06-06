package se.culvertsoft.mgen.compiler.internal

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile

object BuiltInStaticLangGenerator {

   def getModuleFolderPath(module: Module, generatorSettings: java.util.Map[String, String]): String = {
      val pathPrefix_0 = generatorSettings.getOrElse("output_path", "").trim()
      val pathPrefix = (if (pathPrefix_0.nonEmpty) (pathPrefix_0 + "/") else "")
      val folder = pathPrefix + module.path().replaceAllLiterally(".", "/")
      folder
   }

}

abstract class BuiltInStaticLangGenerator extends BuiltInGenerator {

   implicit var currentModule: Module = null

   override def generate(modules: java.util.List[Module], generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {

      val out = new java.util.ArrayList[GeneratedSourceFile]

      // Create package level classes and classregistrys
      for (module <- modules) {
         currentModule = module
         out.addAll(generateModule(module, generatorSettings))
      }

      // Create top level class registry
      // This one should also reference dependencies
      val referencedModules =
         modules
            .flatMap(_.types().values())
            .flatMap(_.getAllReferencedModulesInclSuper())
            .distinct

      out.addAll(generateTopLevelMetaSources(referencedModules, generatorSettings))

      out
   }

   def generateTopLevelMetaSources(
      referencedModules: Seq[Module],
      generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
      val folderPrefixIn = generatorSettings.getOrElse("output_path", "")
      val folderPrefix = if (folderPrefixIn.nonEmpty) folderPrefixIn + "/" else ""
      val packagePath = generatorSettings.asScala.get("classregistry_path").getOrElse(throw new GenerationException("Missing path setting 'classregistry_path'"))
      val folder = folderPrefix + packagePath.replaceAllLiterally(".", "/")
      generateTopLevelMetaSources(folder, packagePath, referencedModules, generatorSettings)
   }

   def generateModule(module: Module, generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {

      val out = new ArrayBuffer[GeneratedSourceFile]

      // Generate classes for this module
      for ((typeName, t) <- module.types())
         out ++= generateClassSources(module, t, generatorSettings)

      // Generate class registry for this module
      if (module.types().nonEmpty)
         out ++= generateModuleMetaSources(module, generatorSettings)

      out
   }

   def generateTopLevelMetaSources(folder: String, packagePath: String, referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile]
   def generateModuleMetaSources(module: Module, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile]
   def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile]

}