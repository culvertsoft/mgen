package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.seqAsJavaList
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator

class CppGenerator extends BuiltInStaticLangGenerator {

  def generateTopLevelMetaSources(
    folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    List(CppClassRegistryHeaderGenerator.generate(folder, packagePath, referencedModules, generatorSettings),
      CppClassRegistrySrcFileGenerator.generate(folder, packagePath, referencedModules, generatorSettings))
  }

  def generateModuleMetaSources(module: Module, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    Nil
  }

  def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    List(CppHeader.generate(module, t, generatorSettings),
      CppSrcFile.generate(module, t, generatorSettings))
  }

}