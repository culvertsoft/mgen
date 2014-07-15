package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator
import se.culvertsoft.mgen.compiler.internal.FancyHeaders
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

abstract class CppSrcFileOrHeader(val fileEnding: String) {
  import CppTypeNames._

  implicit val txtBuffer = new SuperStringBuffer
  implicit var currentModule: Module = null

  def generate(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): GeneratedSourceFile = {
    currentModule = module
    val folder = BuiltInStaticLangGenerator.getModuleFolderPath(module, generatorSettings)
    val fileName = t.shortName() + fileEnding
    val sourceCode = generateSourceCode(module, t, generatorSettings)
    new GeneratedSourceFile(folder + File.separator + fileName, sourceCode)
  }

  def generateSourceCode(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): String = {

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
    
    // Static helper fcns
    mkStaticHelperFcns(t)
    
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
    //mkPrivate()
    mkMetaDataFieldMakers(t)

    // Footer
    mkClassEnd(t)
    mkNamespaceEnd(namespaces)
    mkIncludeGuardEnd()

    txtBuffer.toString()

  }

  def mkIncludeGuardStart(module: Module, t: CustomType) {}

  def getSuperTypeNameString(t: CustomType): String = {
    if (t.hasSuperType()) {
      if (t.superType.module == t.module)
        t.superType.shortName
      else
        t.fullName.replaceAllLiterally(".", "::")
    } else {
      "mgen::MGenBase"
    }
  }

  def mkIncludes(t: CustomType) {}

  def mkNumFieldsSet(t: CustomType) {}

  def mkStaticHelperFcns(t: CustomType) {}
  def mkClassStart(t: CustomType) {}
  def mkPrivate() {}
  def mkMembers(t: CustomType) {}
  def mkPublic() {}
  def mkDefaultCtor(t: CustomType) {}
  def mkRequiredMembersCtor(t: CustomType) {}
  def mkAllMembersCtor(t: CustomType) {}
  def mkDestructor(t: CustomType) {}
  def mkGetters(t: CustomType) {}
  def mkSetters(t: CustomType) {}
  def mkHasers(t: CustomType) {}
  def mkToString(t: CustomType) {}
  def mkHashCode(t: CustomType) {}
  def mkDeepCopy(t: CustomType) {}
  def mkEquals(t: CustomType) {}
  def mkMetadataMethodsComment(t: CustomType) {
    txtBuffer.textln(FancyHeaders.serializationSectionHeader);
  }

  def mkEqOperator(t: CustomType) {}
  def mkTypeName(t: CustomType) {}
  def mkTypeHashes(t: CustomType) {}
  def mkAcceptVisitor(t: CustomType) {}
  def mkDefaultConstructField(t: CustomType) {}
  def mkReadFields(t: CustomType) {}
  def mkReadField(t: CustomType) {}
  def mkGetFields(t: CustomType) {}
  def mkFieldById(t: CustomType) {}
  def mkTypeHierarchyMethods(t: CustomType) {}
  def mkNewInstance(t: CustomType) {}
  def mkMetadataComment(t: CustomType) {
    txtBuffer.textln(FancyHeaders.metadataSectionHeader);
  }
  def mkMetaDataFields(t: CustomType) {}
  def mkMetaDataFieldMakers(t: CustomType) {}
  def mkClassEnd(t: CustomType) {}

  def mkUsingStatements(t: CustomType) {
  }

  def mkSetFieldsSet(t: CustomType) {
  }

  def mkValidate(t: CustomType) {
  }

  def mkIsFieldSet(t: CustomType) {
  }

  def mkNamespaceEnd(namespaces: Array[String]) {
    CppGenUtils.mkNameSpacesEnd(namespaces)
  }

  def mkMetadataGetters(t: CustomType) {

  }

  def mkIncludeGuardEnd() {}

}