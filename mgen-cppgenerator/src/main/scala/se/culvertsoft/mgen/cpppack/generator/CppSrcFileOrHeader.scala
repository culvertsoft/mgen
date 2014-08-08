package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.FancyHeaders
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

abstract class CppSrcFileOrHeader(val fileEnding: String) {
  import CppTypeNames._

  implicit val txtBuffer = SuperStringBuffer.getCached()
  implicit var currentModule: Module = null

  def generate(module: Module, t: ClassType, generatorSettings: java.util.Map[String, String]): GeneratedSourceFile = {
    currentModule = module
    val folder = BuiltInStaticLangGenerator.getModuleFolderPath(module, generatorSettings)
    val fileName = t.shortName() + fileEnding
    val sourceCode = generateSourceCode(module, t, generatorSettings)
    new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
  }

  def generateSourceCode(module: Module, t: ClassType, generatorSettings: java.util.Map[String, String]): String = {

    val namespaces = currentModule.path().split("\\.")

    txtBuffer.clear()

    // Header
    CppGenUtils.mkFancyHeader()
    mkIncludeGuardStart(module, t)
    mkIncludes(t)
    CppGenUtils.mkNameSpaces(namespaces)
    mkUsingStatements(t)

    // Class Begin
    mkClassStart(t)

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

  def mkIncludeGuardStart(module: Module, t: ClassType) {}

  def getSuperTypeNameString(t: ClassType): String = {
    if (t.hasSuperType()) {
      if (t.superType.module == t.module)
        t.superType.shortName
      else
        t.fullName.replaceAllLiterally(".", "::")
    } else {
      "mgen::MGenBase"
    }
  }

  def mkIncludes(t: ClassType) {}

  def mkNumFieldsSet(t: ClassType) {}

  def mkClassStart(t: ClassType) {}
  def mkPrivate() {}
  def mkConstants(t: ClassType) {}
  def mkMembers(t: ClassType) {}
  def mkPublic() {}
  def mkDefaultCtor(t: ClassType) {}
  def mkRequiredMembersCtor(t: ClassType) {}
  def mkAllMembersCtor(t: ClassType) {}
  def mkDestructor(t: ClassType) {}
  def mkGetters(t: ClassType) {}
  def mkSetters(t: ClassType) {}
  def mkHasers(t: ClassType) {}
  def mkToString(t: ClassType) {}
  def mkHashCode(t: ClassType) {}
  def mkDeepCopy(t: ClassType) {}
  def mkEquals(t: ClassType) {}
  def mkMetadataMethodsComment(t: ClassType) {
    txtBuffer.textln(FancyHeaders.serializationSectionHeader);
  }

  def mkEqOperator(t: ClassType) {}
  def mkTypeName(t: ClassType) {}
  def mkTypeHashes(t: ClassType) {}
  def mkAcceptVisitor(t: ClassType) {}
  def mkDefaultConstructField(t: ClassType) {}
  def mkReadFields(t: ClassType) {}
  def mkReadField(t: ClassType) {}
  def mkGetFields(t: ClassType) {}
  def mkFieldById(t: ClassType) {}
  def mkTypeHierarchyMethods(t: ClassType) {}
  def mkNewInstance(t: ClassType) {}
  def mkMetadataComment(t: ClassType) {
    txtBuffer.textln(FancyHeaders.metadataSectionHeader);
  }
  def mkMetaDataFields(t: ClassType) {}
  def mkClassEnd(t: ClassType) {}

  def mkUsingStatements(t: ClassType) {
  }

  def mkSetFieldsSet(t: ClassType) {
  }

  def mkValidate(t: ClassType) {
  }

  def mkIsFieldSet(t: ClassType) {
  }

  def mkNamespaceEnd(namespaces: Array[String]) {
    CppGenUtils.mkNameSpacesEnd(namespaces)
  }

  def mkMetadataGetters(t: ClassType) {

  }

  def mkIncludeGuardEnd() {}

}