package se.culvertsoft.mgen.javapack.generator

import java.io.File

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator.getModuleFolderPath
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.makers.MkAcceptVisitor
import se.culvertsoft.mgen.javapack.generator.makers.MkAllMembersCtor
import se.culvertsoft.mgen.javapack.generator.makers.MkClassEnd
import se.culvertsoft.mgen.javapack.generator.makers.MkClassRegistry
import se.culvertsoft.mgen.javapack.generator.makers.MkClassStart
import se.culvertsoft.mgen.javapack.generator.makers.MkDeepCopy
import se.culvertsoft.mgen.javapack.generator.makers.MkDefaultCtor
import se.culvertsoft.mgen.javapack.generator.makers.MkEquals
import se.culvertsoft.mgen.javapack.generator.makers.MkFancyHeader
import se.culvertsoft.mgen.javapack.generator.makers.MkFancyHeader.MkMetadataComment
import se.culvertsoft.mgen.javapack.generator.makers.MkFancyHeader.MkMetadataMethodsComment
import se.culvertsoft.mgen.javapack.generator.makers.MkFieldById
import se.culvertsoft.mgen.javapack.generator.makers.MkFieldMetaData
import se.culvertsoft.mgen.javapack.generator.makers.MkGetFields
import se.culvertsoft.mgen.javapack.generator.makers.MkGetters
import se.culvertsoft.mgen.javapack.generator.makers.MkHashCode
import se.culvertsoft.mgen.javapack.generator.makers.MkImports
import se.culvertsoft.mgen.javapack.generator.makers.MkIsFieldSet
import se.culvertsoft.mgen.javapack.generator.makers.MkMarkFieldsSet
import se.culvertsoft.mgen.javapack.generator.makers.MkMembers
import se.culvertsoft.mgen.javapack.generator.makers.MkModuleClassRegistry
import se.culvertsoft.mgen.javapack.generator.makers.MkNFieldsSet
import se.culvertsoft.mgen.javapack.generator.makers.MkPackage
import se.culvertsoft.mgen.javapack.generator.makers.MkReadField
import se.culvertsoft.mgen.javapack.generator.makers.MkRequiredMembersCtor
import se.culvertsoft.mgen.javapack.generator.makers.MkSetters
import se.culvertsoft.mgen.javapack.generator.makers.MkToString
import se.culvertsoft.mgen.javapack.generator.makers.MkTypeIdFields
import se.culvertsoft.mgen.javapack.generator.makers.MkTypeIdMethods
import se.culvertsoft.mgen.javapack.generator.makers.MkValidate

class JavaGenerator extends BuiltInStaticLangGenerator {

  implicit val txtBuffer = new SuperStringBuffer

  override def generateTopLevelMetaSources(
    folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val fileName = "MGenClassRegistry" + ".java"
    val sourceCode = MkClassRegistry(referencedModules, packagePath)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateModuleMetaSources(
    module: Module,
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = "MGenModuleClassRegistry" + ".java"
    val sourceCode = MkModuleClassRegistry(module)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = t.name() + ".java"
    val sourceCode = generateClassSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  def generateClassSourceCode(t: CustomType): String = {

    txtBuffer.clear()

    MkFancyHeader(t)

    MkPackage(currentModule)
    MkImports(t, currentModule)
    MkClassStart(t, currentModule)

    MkMembers(t, currentModule)
    MkDefaultCtor(t, currentModule)
    MkRequiredMembersCtor(t, currentModule)
    MkAllMembersCtor(t, currentModule)
    MkGetters(t, currentModule)
    MkSetters(t, currentModule)
    MkToString(t, currentModule)
    MkHashCode(t, currentModule)
    MkEquals(t, currentModule)
    MkDeepCopy(t, currentModule)

    MkMetadataMethodsComment(t)

    MkTypeIdMethods(t, currentModule)
    MkAcceptVisitor(t, currentModule)
    MkReadField(t, currentModule)
    MkGetFields(t, currentModule)
    MkIsFieldSet(t, currentModule)
    MkMarkFieldsSet(t, currentModule)
    MkValidate(t, currentModule)
    MkNFieldsSet(t, currentModule)
    MkFieldById(t, currentModule)

    MkMetadataComment(t)

    MkTypeIdFields(t, currentModule)
    MkFieldMetaData(t, currentModule)
    MkClassEnd()

    txtBuffer.toString()
  }

}