package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

abstract class CppClassRegistryGenerator(fileEnding: String) {

  implicit val txtBuffer = new SuperStringBuffer
  var namespacesstring = ""

  def generate(folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): GeneratedSourceFile = {
    val fileName = "ClassRegistry" + fileEnding
    val sourceCode = generateSourceCode(packagePath, referencedModules, generatorSettings)
    new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
  }

  def generateSourceCode(
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): String = {

    val namespaces = packagePath.split("\\.")
    namespacesstring = namespaces.mkString("::")

    txtBuffer.clear()

    CppGenUtils.mkFancyHeader()
    mkIncludes(referencedModules, generatorSettings)
    CppGenUtils.mkNameSpaces(namespaces)
    mkClassStart(referencedModules, generatorSettings)

    mkDefaultCtor(referencedModules, generatorSettings)
    mkDestructor(referencedModules, generatorSettings)
    
    mkReadObjectFields(referencedModules, generatorSettings)
    mkVisitObjectFields(referencedModules, generatorSettings)
    
    mkGetByTypeIds16Bit(referencedModules, generatorSettings)
    mkGetByTypeIds16BitBase64(referencedModules, generatorSettings)
    
    mkClassEnd(referencedModules, generatorSettings)
    CppGenUtils.mkNameSpacesEnd(namespaces)

    txtBuffer.toString

  }

  def mkIncludes(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkClassStart(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkDefaultCtor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkDestructor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkReadObjectFields(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkVisitObjectFields(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkGetByTypeIds16Bit(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkGetByTypeIds16BitBase64(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

  def mkClassEnd(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
  }

}