package se.culvertsoft.mgen.compiler.internal

import scala.annotation.migration
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.mutableSeqAsJavaList

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Generator

object BuiltInStaticLangGenerator {

  def getModuleFolderPath(module: Module, generatorSettings: Map[String, String]): String = {
    val pathPrefix_0 = generatorSettings.getOrElse("output_path", "").trim()
    val pathPrefix = (if (pathPrefix_0.nonEmpty) (pathPrefix_0 + "/") else "")
    val folder = pathPrefix + module.path().replaceAllLiterally(".", "/")
    folder
  }

}

abstract class BuiltInStaticLangGenerator extends Generator {

  override def generate(project: Project, generatorSettings: java.util.Map[String, String]): java.util.List[GeneratedSourceFile] = {
    val modules = project.allModulesRecursively()
    val sources = modules.flatMap(generateSources(_, generatorSettings.toMap))
    val metaSources = generateMetaSources(modules, generatorSettings.toMap)
    (sources ++ metaSources).par.map(_.apply()).seq
  }

  final def generateSources(module: Module, generatorSettings: Map[String, String]): Seq[CodeGenerationTask] = {
    val enumSources = module.enums.flatMap(generateEnumSources(module, _, generatorSettings))
    val typesSources = module.classes.flatMap(generateClassSources(module, _, generatorSettings))
    enumSources ++ typesSources
  }

  final def generateMetaSources(referencedModules: Seq[Module], generatorSettings: Map[String, String]): Seq[CodeGenerationTask] = {
    val folderPrefixIn = generatorSettings.getOrElse("output_path", "")
    val folderPrefix = if (folderPrefixIn.nonEmpty) folderPrefixIn + "/" else ""
    val packagePath = generatorSettings.getOrElse("classregistry_path", throw new GenerationException("Missing path setting 'classregistry_path'"))
    val folder = folderPrefix + packagePath.replaceAllLiterally(".", "/")
    generateMetaSources(folder, packagePath, referencedModules, generatorSettings)
  }

  def generateMetaSources(folder: String, packagePath: String, referencedModules: Seq[Module], generatorSettings: Map[String, String]): Seq[CodeGenerationTask] = { Nil }
  def generateClassSources(module: Module, t: ClassType, generatorSettings: Map[String, String]): Seq[CodeGenerationTask] = { Nil }
  def generateEnumSources(module: Module, t: EnumType, generatorSettings: Map[String, String]): Seq[CodeGenerationTask] = { Nil }

}