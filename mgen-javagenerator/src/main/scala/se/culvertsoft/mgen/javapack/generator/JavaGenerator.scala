package se.culvertsoft.mgen.javapack.generator

import java.io.File
import scala.collection.JavaConversions.seqAsJavaList
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator.getModuleFolderPath
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.impl.MkAcceptVisitor
import se.culvertsoft.mgen.javapack.generator.impl.MkAllMembersCtor
import se.culvertsoft.mgen.javapack.generator.impl.MkClassEnd
import se.culvertsoft.mgen.javapack.generator.impl.MkClassRegistry
import se.culvertsoft.mgen.javapack.generator.impl.MkClassStart
import se.culvertsoft.mgen.javapack.generator.impl.MkDeepCopy
import se.culvertsoft.mgen.javapack.generator.impl.MkDefaultCtor
import se.culvertsoft.mgen.javapack.generator.impl.MkDispatcher
import se.culvertsoft.mgen.javapack.generator.impl.MkEquals
import se.culvertsoft.mgen.javapack.generator.impl.MkFancyHeader
import se.culvertsoft.mgen.javapack.generator.impl.MkFancyHeader.MkMetadataComment
import se.culvertsoft.mgen.javapack.generator.impl.MkFancyHeader.MkMetadataMethodsComment
import se.culvertsoft.mgen.javapack.generator.impl.MkFieldById
import se.culvertsoft.mgen.javapack.generator.impl.MkFieldMetaData
import se.culvertsoft.mgen.javapack.generator.impl.MkGetFields
import se.culvertsoft.mgen.javapack.generator.impl.MkGetters
import se.culvertsoft.mgen.javapack.generator.impl.MkHandler
import se.culvertsoft.mgen.javapack.generator.impl.MkHashCode
import se.culvertsoft.mgen.javapack.generator.impl.MkImports
import se.culvertsoft.mgen.javapack.generator.impl.MkIsFieldSet
import se.culvertsoft.mgen.javapack.generator.impl.MkMarkFieldsSet
import se.culvertsoft.mgen.javapack.generator.impl.MkMembers
import se.culvertsoft.mgen.javapack.generator.impl.MkNFieldsSet
import se.culvertsoft.mgen.javapack.generator.impl.MkPackage
import se.culvertsoft.mgen.javapack.generator.impl.MkReadField
import se.culvertsoft.mgen.javapack.generator.impl.MkRequiredMembersCtor
import se.culvertsoft.mgen.javapack.generator.impl.MkSetters
import se.culvertsoft.mgen.javapack.generator.impl.MkToString
import se.culvertsoft.mgen.javapack.generator.impl.MkTypeIdFields
import se.culvertsoft.mgen.javapack.generator.impl.MkTypeIdMethods
import se.culvertsoft.mgen.javapack.generator.impl.MkValidate
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.PrimitiveType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.javapack.generator.impl.MkEnum

object JavaGenerator {
  def canBeNull(f: Field): Boolean = {
    canBeNull(f.typ())
  }
  def canBeNull(t: Type): Boolean = {
    t match {
      case t: PrimitiveType => false
      case _ => true
    }
  }
}

class JavaGenerator extends BuiltInStaticLangGenerator {

  implicit val txtBuffer = SuperStringBuffer.getCached()

  override def generateMetaSources(
    folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {

    def mkClasReg(): GeneratedSourceFile = {
      val fileName = "ClassRegistry" + ".java"
      val sourceCode = MkClassRegistry(referencedModules, packagePath)
      new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
    }

    def mkDispatcher(): GeneratedSourceFile = {
      val fileName = "Dispatcher" + ".java"
      val sourceCode = MkDispatcher(referencedModules, packagePath)
      new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
    }

    def mkHandler(): GeneratedSourceFile = {
      val fileName = "Handler" + ".java"
      val sourceCode = MkHandler(referencedModules, packagePath)
      new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
    }

    val clsRegistry = mkClasReg()
    val dispatcher = mkDispatcher()
    val handler = mkHandler()

    List(clsRegistry, dispatcher, handler)

  }

  override def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = t.name() + ".java"
    val sourceCode = generateClassSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateEnumSources(module: Module, t: EnumType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = t.shortName() + ".java"
    val sourceCode = generateEnumSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  def generateClassSourceCode(t: CustomType): String = {
    txtBuffer.clear()
    mkPublicSection(t)
    mkMetadataMethodsSection(t)
    mkMetadataFieldsSection(t)
    txtBuffer.toString()
  }

  def generateEnumSourceCode(t: EnumType): String = {
    MkEnum(t, t.module.path)
  }

  def mkPublicSection(t: CustomType) {
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
  }

  def mkMetadataMethodsSection(t: CustomType) {
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
  }

  def mkMetadataFieldsSection(t: CustomType) {
    MkMetadataComment(t)
    MkTypeIdFields(t, currentModule)
    MkFieldMetaData(t, currentModule)
    MkClassEnd()
  }

}