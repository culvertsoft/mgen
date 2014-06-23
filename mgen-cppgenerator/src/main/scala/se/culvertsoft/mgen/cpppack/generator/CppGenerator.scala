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

    val classRegH = CppClassRegistryHeaderGenerator.generate(folder, packagePath, referencedModules, generatorSettings)
    val classRegCpp = CppClassRegistrySrcFileGenerator.generate(folder, packagePath, referencedModules, generatorSettings)

    val dispatcherH = CppDispatchHeaderGenerator.generate(folder, packagePath, referencedModules, generatorSettings)
    val dispatcherCpp = CppDispatchSrcFileGenerator.generate(folder, packagePath, referencedModules, generatorSettings)

    val handlerH = CppHandlerHeaderGenerator.generate(folder, packagePath, referencedModules, generatorSettings)
    val handlerCpp = CppHandlerSrcFileGenerator.generate(folder, packagePath, referencedModules, generatorSettings)

    val fwdDeclare = ForwardDeclareGenerator.generate(folder, packagePath, referencedModules, generatorSettings)
    
    List(
      classRegH,
      classRegCpp,
      dispatcherH,
      dispatcherCpp,
      handlerH,
      handlerCpp,
      fwdDeclare)

  }

  def generateModuleMetaSources(module: Module, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    Nil
  }

  def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    List(CppHeader.generate(module, t, generatorSettings),
      CppSrcFile.generate(module, t, generatorSettings))
  }

}