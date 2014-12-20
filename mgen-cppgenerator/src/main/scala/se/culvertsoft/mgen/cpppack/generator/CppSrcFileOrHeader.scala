package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.FancyHeaders
import se.culvertsoft.mgen.compiler.util.SettingsUtils.RichSettings
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

abstract class CppSrcFileOrHeader(val fileEnding: String) {

  def generate(t: ClassType, settings: java.util.Map[String, String]): GeneratedSourceFile = {
    val folder = BuiltInStaticLangGenerator.getModuleFolderPath(t.module, settings)
    val fileName = t.shortName() + fileEnding
    val genCustomCodeSections = settings.getBool("generate_custom_code_sections").getOrElse(true)
    val sourceCode = generateSourceCode(t, genCustomCodeSections)
    new GeneratedSourceFile(folder + File.separator + fileName, sourceCode, CppGenerator.getCustomCodeSections(genCustomCodeSections))
  }

  def generateSourceCode(t: ClassType, genCustomCodeSections: Boolean): String = {

    implicit val module = t.module
    implicit val txtBuffer = SourceCodeBuffer.getThreadLocal()
    
    val namespaces = module.path().split("\\.")
 
    txtBuffer.clear()

    // Header
    CppGenUtils.mkFancyHeader()
    mkIncludeGuardStart(module, t)
    mkIncludes(t, genCustomCodeSections)
    CppGenUtils.mkNameSpaces(namespaces)
    mkUsingStatements(t)

    // Class Begin
    mkClassStart(t, genCustomCodeSections)

    // Normal class api section
    mkConstants(t)
    mkPrivate()
    mkMembers(t)
    mkPublic()
    mkDefaultCtor(t)
    mkRequiredMembersCtor(t)
    mkAllMembersCtor(t)
    mkDestructor(t)
    mkGetters(t)
    mkSetters(t)
    if (genCustomCodeSections)
      mkCustomPublicMethodsSection(t)
    mkHasers(t)
    mkEqOperator(t)
    mkToString(t)
    mkHashCode(t)

    // Metadata methods section
    mkMetadataMethodsComment(t)

    mkPublic()
    mkReadField(t)
    mkAcceptVisitor(t)
    mkFieldById(t)
    mkMetadataGetters(t)
    mkSetFieldsSet(t)
    mkNumFieldsSet(t)
    mkIsFieldSet(t)
    mkValidate(t)
    mkEquals(t)
    mkDeepCopy(t)
    mkNewInstance(t)

    // Metadata data section
    mkMetadataComment(t)
    mkMetaDataFields(t)

    // Footer
    mkClassEnd(t)
    mkNamespaceEnd(namespaces)
    mkIncludeGuardEnd()

    txtBuffer.toString()

  }

  def mkIncludeGuardStart(module: Module, t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}

  def getSuperTypeNameString(t: ClassType): String = {
    if (t.hasSuperType()) {
      if (t.superType.module == t.module)
        t.superType.shortName
      else
        t.superType.fullName.replaceAllLiterally(".", "::")
    } else {
      "mgen::MGenBase"
    }
  }

  def mkIncludes(t: ClassType, genCustomCodeSections: Boolean = false)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkNumFieldsSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkCustomPublicMethodsSection(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkClassStart(t: ClassType, genCustomCodeSections: Boolean = false)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkPrivate()(implicit txtBuffer: SourceCodeBuffer) {}
  def mkConstants(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkMembers(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkPublic()(implicit txtBuffer: SourceCodeBuffer) {}
  def mkDefaultCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkRequiredMembersCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkAllMembersCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkDestructor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkGetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkSetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkHasers(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkToString(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkHashCode(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkDeepCopy(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkEquals(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkMetadataMethodsComment(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    txtBuffer.textln(FancyHeaders.serializationSectionHeader);
  }

  def mkEqOperator(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkTypeName(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkTypeHashes(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkAcceptVisitor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkDefaultConstructField(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkReadFields(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkReadField(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkGetFields(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkFieldById(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkTypeHierarchyMethods(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkNewInstance(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkMetadataComment(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    txtBuffer.textln(FancyHeaders.metadataSectionHeader);
  }
  def mkMetaDataFields(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}
  def mkClassEnd(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkUsingStatements(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
  }

  def mkSetFieldsSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
  }

  def mkValidate(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
  }

  def mkIsFieldSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
  }

  def mkNamespaceEnd(namespaces: Array[String])(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.mkNameSpacesEnd(namespaces)
  }

  def mkMetadataGetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

  }

  def mkIncludeGuardEnd()(implicit txtBuffer: SourceCodeBuffer) {}

}