package se.culvertsoft.mgen.pythongenerator

import scala.collection.JavaConversions.seqAsJavaList
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.CustomCodeSection
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator.getModuleFolderPath
import se.culvertsoft.mgen.compiler.util.SettingsUtils.RichSettings
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import java.io.File

object PythonGenerator {

  def mkCustomCodeSection(name: String): CustomCodeSection = {
    def mkKey(ending: String) = s"# ${name}_${ending}"
    new CustomCodeSection(mkKey("begin"), mkKey("end"))
  }

  val custom_imports_section = mkCustomCodeSection("custom_modules")
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

class PythonGenerator extends BuiltInStaticLangGenerator {
  import PythonGenerator.getCustomCodeSections
  import BuiltInStaticLangGenerator._

  override def generateMetaSources(
    folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {

    implicit val txtBuffer = SourceCodeBuffer.getThreadLocal()

    /*
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
*/
    List()
  }

  override def generateClassSources(module: Module, t: ClassType, settings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, settings)
    val fileName = t.shortName + ".py"
    val generateCustomCodeSections = settings.getBool("generate_custom_code_sections").getOrElse(true)
    val sourceCode = generateClassSourceCode(t, generateCustomCodeSections)
    List(new GeneratedSourceFile(
      folder + File.separator + fileName,
      sourceCode,
      getCustomCodeSections(generateCustomCodeSections)))
  }

  override def generateEnumSources(module: Module, t: EnumType, settings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    /*
    val folder = getModuleFolderPath(module, settings)
    val fileName = t.shortName() + ".java"
    val sourceCode = generateEnumSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
    */
    List()
  }

  def generateClassSourceCode(t: ClassType, generateCustomCodeSections: Boolean): String = {
    implicit val txtBuffer = SourceCodeBuffer.getThreadLocal()

    txtBuffer.clear()
    txtBuffer.textln(PythonConstants.fileHeader)
    txtBuffer.toString()
  }

}