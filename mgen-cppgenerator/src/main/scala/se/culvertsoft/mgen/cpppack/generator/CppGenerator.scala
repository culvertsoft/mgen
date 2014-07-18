package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkEnumCpp
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkEnumHeader

object CppGenerator {

  def canBeNull(f: Field): Boolean = {
    f.typ.isInstanceOf[CustomType] && f.isPolymorphic()
  }

  def writeInitializerList(list: Seq[String])(implicit txtBuffer: SuperStringBuffer) {
    if (list.nonEmpty) {
      ln(" : ")
      for ((s, i) <- list.zipWithIndex) {
        if (i + 1 < list.length)
          ln(2, s"$s,")
        else
          txt(2, s)
      }
    }
  }

}

class CppGenerator extends BuiltInStaticLangGenerator {

  override def generateMetaSources(
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

  override def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    List(CppHeader.generate(module, t, generatorSettings),
      CppSrcFile.generate(module, t, generatorSettings))
  }

  override def generateEnumSources(module: Module, t: EnumType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = BuiltInStaticLangGenerator.getModuleFolderPath(module, generatorSettings)
    val hFileName = t.shortName + ".h"
    val hSourceCode = MkEnumHeader(module, t, generatorSettings)
    val cppFileName = t.shortName + ".cpp"
    val cppSourceCode = MkEnumCpp(module, t, generatorSettings)
    List(
      new GeneratedSourceFile(folder + File.separator + hFileName, hSourceCode),
      new GeneratedSourceFile(folder + File.separator + cppFileName, cppSourceCode))
  }

}