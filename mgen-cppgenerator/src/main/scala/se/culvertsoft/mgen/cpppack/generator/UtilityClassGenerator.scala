package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import scala.collection.JavaConversions.mapAsScalaMap

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

abstract class CppArtifactType
case object Header extends CppArtifactType
case object SrcFile extends CppArtifactType

abstract class UtilityClassGenerator(
  className: String,
  superTypeFullName: Option[String],
  artifactType: CppArtifactType) {

  def isHeader(): Boolean = artifactType == Header
  def isSrcFile(): Boolean = artifactType == SrcFile

  implicit val txtBuffer = new SuperStringBuffer

  case class UtilClassGenParam(
    packagePath: String,
    modules: Seq[Module],
    settings: Map[String, String],
    nameSpaces: Seq[String],
    nameSpaceString: String)

  final def generate(folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): GeneratedSourceFile = {
    val sourceCode = generateSourceCode(packagePath, referencedModules, generatorSettings)
    val fileName = className + (if (isHeader) ".h" else ".cpp")
    new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
  }

  final def generateSourceCode(
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): String = {

    val nameSpaces = packagePath.split("\\.")
    val nameSpacesString = nameSpaces.mkString("::")

    val param = UtilClassGenParam(
      packagePath,
      referencedModules,
      generatorSettings.toMap,
      nameSpaces,
      nameSpacesString)

    txtBuffer.clear()

    mkHeader(param)
    mkIncludeGuardStart(param)
    mkIncludes(param)
    mkNamespaceStart(param)
    mkClassStart(param)
    mkClassContents(param)
    mkClassEnd(param)
    mkNamespaceEnd(param)
    mkIncludeGuardEnd(param)

    txtBuffer.toString

  }

  def mkIncludeGuardStart(param: UtilClassGenParam) {
    if (isHeader) {
      CppGenUtils.mkIncludeGuardStart(s"${param.nameSpaceString}::$className")
    }
  }

  def mkHeader(param: UtilClassGenParam) {
    CppGenUtils.mkFancyHeader()
  }

  def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include("mgen/classes/MGenBase.h")
  }

  def mkNamespaceStart(param: UtilClassGenParam) {
    CppGenUtils.mkNameSpaces(param.nameSpaces)
  }

  def mkClassStart(param: UtilClassGenParam) {
    if (isHeader) {
      CppGenUtils.mkClassStart(className, superTypeFullName.getOrElse(""))
    }
  }

  def mkClassContents(param: UtilClassGenParam) {}

  def mkClassEnd(param: UtilClassGenParam) {
    if (isHeader) {
      txtBuffer.textln(s"}; // End class $className").endl()
    }
  }

  def mkNamespaceEnd(param: UtilClassGenParam) {
    CppGenUtils.mkNameSpacesEnd(param.nameSpaces)
  }

  def mkIncludeGuardEnd(param: UtilClassGenParam) {
    if (isHeader) {
      CppGenUtils.mkIncludeGuardEnd()
    }
  }

}