package se.culvertsoft.mgen.compiler.internal

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.JavaConverters.mapAsScalaMapConverter

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Generator

object BuiltInStaticLangGenerator {

  def getModuleFolderPath(module: Module, generatorSettings: java.util.Map[String, String]): String = {
    val pathPrefix_0 = generatorSettings.getOrElse("output_path", "").trim()
    val pathPrefix = (if (pathPrefix_0.nonEmpty) (pathPrefix_0 + "/") else "")
    val folder = pathPrefix + module.path().replaceAllLiterally(".", "/")
    folder
  }

}

abstract class BuiltInStaticLangGenerator extends Generator {

  implicit var currentModule: Module = null

  override def generate(project: Project, generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {
    val modules = project.allModulesRecursively()
    val sources = modules.flatMap(generateSources(_, generatorSettings))
    val metaSources = generateMetaSources(modules, generatorSettings)
    sources ++ metaSources
  }

  def generateSources(module: Module, generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {
    currentModule = module
    val enumSources = module.enums.flatMap(e => generateEnumSources(module, e, generatorSettings))
    val typesSources = module.types.flatMap(t => generateClassSources(module, t, generatorSettings))
    enumSources ++ typesSources
  }

  def generateMetaSources(
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folderPrefixIn = generatorSettings.getOrElse("output_path", "")
    val folderPrefix = if (folderPrefixIn.nonEmpty) folderPrefixIn + "/" else ""
    val packagePath = generatorSettings.asScala.get("classregistry_path").getOrElse(throw new GenerationException("Missing path setting 'classregistry_path'"))
    val folder = folderPrefix + packagePath.replaceAllLiterally(".", "/")
    generateMetaSources(folder, packagePath, referencedModules, generatorSettings)
  }

  def generateMetaSources(folder: String, packagePath: String, referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = { Nil }
  def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = { Nil }
  def generateEnumSources(module: Module, t: EnumType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = { Nil }

}