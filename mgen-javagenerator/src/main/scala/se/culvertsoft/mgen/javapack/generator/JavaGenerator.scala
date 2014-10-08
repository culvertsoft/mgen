package se.culvertsoft.mgen.javapack.generator

import java.io.File

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.CustomCodeSection
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.PrimitiveType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator.getModuleFolderPath
import se.culvertsoft.mgen.compiler.util.SettingsUtils.RichSettings
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.impl.MkAcceptVisitor
import se.culvertsoft.mgen.javapack.generator.impl.MkAllMembersCtor
import se.culvertsoft.mgen.javapack.generator.impl.MkClassEnd
import se.culvertsoft.mgen.javapack.generator.impl.MkClassRegistry
import se.culvertsoft.mgen.javapack.generator.impl.MkClassStart
import se.culvertsoft.mgen.javapack.generator.impl.MkDeepCopy
import se.culvertsoft.mgen.javapack.generator.impl.MkDefaultCtor
import se.culvertsoft.mgen.javapack.generator.impl.MkDispatcher
import se.culvertsoft.mgen.javapack.generator.impl.MkEnum
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

  def mkCustomCodeSection(name: String): CustomCodeSection = {
    def mkKey(ending: String) = s"/* ${name}_${ending} */"
    new CustomCodeSection(mkKey("begin"), mkKey("end"))
  }

  val custom_imports_section = mkCustomCodeSection("custom_imports")
  val custom_interfaces_section = mkCustomCodeSection("custom_ifcs")
  val custom_methods_section = mkCustomCodeSection("custom_methods")

  val customClassCodeSections = List(
    custom_imports_section,
    custom_interfaces_section,
    custom_methods_section)

  def getCustomCodeSections(getCustomCodeSections: Boolean): Seq[CustomCodeSection] = {
    if (getCustomCodeSections)
      customClassCodeSections
    else
      Nil
  }

}

class JavaGenerator extends BuiltInStaticLangGenerator {
  import JavaGenerator.getCustomCodeSections

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

  override def generateClassSources(module: Module, t: ClassType, settings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, settings)
    val fileName = t.shortName + ".java"
    val generateCustomCodeSections = settings.getBool("generate_custom_code_sections").getOrElse(true)
    val sourceCode = generateClassSourceCode(t, generateCustomCodeSections)
    List(new GeneratedSourceFile(
      folder + File.separator + fileName,
      sourceCode,
      getCustomCodeSections(generateCustomCodeSections)))
  }

  override def generateEnumSources(module: Module, t: EnumType, settings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, settings)
    val fileName = t.shortName() + ".java"
    val sourceCode = generateEnumSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  def generateClassSourceCode(t: ClassType, genCustomCodeSections: Boolean): String = {
    txtBuffer.clear()
    mkPublicSection(t, genCustomCodeSections)
    mkMetadataMethodsSection(t)
    mkMetadataFieldsSection(t)
    txtBuffer.toString()
  }

  def generateEnumSourceCode(t: EnumType): String = {
    MkEnum(t, t.module.path)
  }

  def mkPublicSection(t: ClassType, genCustomCodeSections: Boolean) {
    MkFancyHeader(t)
    MkPackage(currentModule)
    MkImports(t, currentModule, genCustomCodeSections)
    MkClassStart(t, currentModule, genCustomCodeSections)
    MkMembers(t, currentModule)
    MkDefaultCtor(t, currentModule)
    MkRequiredMembersCtor(t, currentModule)
    MkAllMembersCtor(t, currentModule)
    MkGetters(t, currentModule)
    MkSetters(t, currentModule)

    if (genCustomCodeSections) {
      ln(1, JavaGenerator.custom_methods_section.toString)
      endl()
    }

    MkToString(t, currentModule)
    MkHashCode(t, currentModule)
    MkEquals(t, currentModule)
    MkDeepCopy(t, currentModule)
  }

  def mkMetadataMethodsSection(t: ClassType) {
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

  def mkMetadataFieldsSection(t: ClassType) {
    MkMetadataComment(t)
    MkTypeIdFields(t, currentModule)
    MkFieldMetaData(t, currentModule)
    MkClassEnd()
  }

}