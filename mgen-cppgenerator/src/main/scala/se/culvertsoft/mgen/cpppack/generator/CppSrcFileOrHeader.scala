package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.generator.GeneratedSourceFile
import se.culvertsoft.mgen.api.types.Field
import se.culvertsoft.mgen.api.types.Module
import se.culvertsoft.mgen.api.types.CustomType
import se.culvertsoft.mgen.api.types.Type
import se.culvertsoft.mgen.api.types.TypeEnum
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.internal.BuiltInGenerator.upFirst
import se.culvertsoft.mgen.compiler.internal.BuiltInJavaCppGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator

abstract class CppSrcFileOrHeader(val fileEnding: String) {
   import CppTypeNames._

   implicit val txtBuffer = new SuperStringBuffer
   implicit var currentModule: Module = null

   def generate(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): GeneratedSourceFile = {
      currentModule = module
      val folder = BuiltInStaticLangGenerator.getModuleFolderPath(module, generatorSettings)
      val fileName = t.shortName() + fileEnding
      val sourceCode = generateSourceCode(module, t, generatorSettings)
      new GeneratedSourceFile(folder, fileName, sourceCode)
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
      mkClassStart(t)

      // Normal class api section
      mkPrivate()
      mkMembers(t)
      mkPublic()
      mkDefaultCtor(t)
      mkRequiredMembersCtor(t)
      mkAllMembersAsArgsCtor(t)
      mkDestructor(t)
      mkGetters(t)
      mkSetters(t)
      mkEqOperator(t)
      mkToString(t)
      mkHashCode(t)

      // Metadata methods section
      mkMetadataMethodsComment(t)
      mkPublic()
      mkReadField(t)
      mkAcceptVisitor(t)
      mkFieldBy16BitHash(t)
      mkFieldBy32BitHash(t)
      mkFieldByName(t)
      mkMetadataGetters(t)
      mkSetFieldsSet(t)
      mkValidate(t)
      mkNewInstance(t)
      mkEquals(t)
      mkDeepCopy(t)

      // Metadata data section
      mkMetadataComment(t)
      mkMetaDataFields(t)
      mkNFieldsSet(t)
      mkFieldStatusGetters(t)
      mkPrivate()
      mkMetaDataFieldMakers(t)

      // Footer
      mkClassEnd(t)
      mkNamespaceEnd(namespaces)
      mkIncludeGuardEnd()

      txtBuffer.toString()

   }

   def mkIncludeGuardStart(module: Module, t: CustomType) {}

   def getQualifiedClassNameOf(t: Type): String = {
      if (t.typeEnum() == TypeEnum.MGEN_BASE) {
         "mgen::MGenBase"
      } else {
         t.fullName().replaceAllLiterally(".", "::")
      }
   }

   def getSuperTypeNameString(t: CustomType): String = {
      if (t.superType().typeEnum() == TypeEnum.CUSTOM) {
         val superModuleSameAsOurs = t.superType().asInstanceOf[CustomType].module() == t.module();
         val superNameString = if (superModuleSameAsOurs) t.superType().shortName() else getQualifiedClassNameOf(t.superType())
         superNameString
      } else {
         getQualifiedClassNameOf(t.superType())
      }
   }

   def mkIncludes(t: CustomType) {}

   def mkNFieldsSet(t: CustomType) {}
   
   def mkClassStart(t: CustomType) {}
   def mkPrivate() {}
   def mkMembers(t: CustomType) {}
   def mkPublic() {}
   def mkDefaultCtor(t: CustomType) {}
   def mkRequiredMembersCtor(t: CustomType) {}
   def mkAllMembersAsArgsCtor(t: CustomType) {}
   def mkDestructor(t: CustomType) {}
   def mkGetters(t: CustomType) {}
   def mkSetters(t: CustomType) {}
   def mkToString(t: CustomType) {}
   def mkHashCode(t: CustomType) {}
   def mkDeepCopy(t: CustomType) {}
   def mkEquals(t: CustomType) {}
   def mkMetadataMethodsComment(t: CustomType) {
      txtBuffer.textln(BuiltInJavaCppGenerator.serializationSectionHeader);
   }

   def mkEqOperator(t: CustomType) {}
   def mkTypeName(t: CustomType) {}
   def mkTypeHashes(t: CustomType) {}
   def mkAcceptVisitor(t: CustomType) {}
   def mkDefaultConstructField(t: CustomType) {}
   def mkReadFields(t: CustomType) {}
   def mkReadField(t: CustomType) {}
   def mkGetFields(t: CustomType) {}
   def mkFieldBy16BitHash(t: CustomType) {}
   def mkFieldBy32BitHash(t: CustomType) {}
   def mkFieldByName(t: CustomType) {}
   def mkTypeHierarchyMethods(t: CustomType) {}
   def mkNewInstance(t: CustomType) {}
   def mkMetadataComment(t: CustomType) {
      txtBuffer.textln(BuiltInJavaCppGenerator.metadataSectionHeader);
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
   
   def mkFieldStatusGetters(t: CustomType) {
   }

   def mkNamespaceEnd(namespaces: Array[String]) {
      CppGenUtils.mkNameSpacesEnd(namespaces)
   }

   def mkMetadataGetters(t: CustomType) {

   }

   def mkIncludeGuardEnd() {}

   def get(field: Field): String = {
      s"get${upFirst(field.name())}()"
   }

   def getMutable(field: Field): String = {
      s"get${upFirst(field.name())}Mutable()"
   }

   def set(field: Field, input: String): String = {
      s"set${upFirst(field.name())}($input)"
   }

   def hash16(field: Field): String = {
      s"_${field.name()}_HASH_16BIT"
   }

   def hash32(field: Field): String = {
      s"_${field.name()}_HASH_32BIT"
   }

   def meta(field: Field, inclParan: Boolean = true): String = {
      if (inclParan)
         s"_${field.name()}_METADATA()"
      else
         s"_${field.name()}_METADATA"
   }

   def isSetName(f: Field): String = {
      s"_m_${f.name()}_isSet"
   }

   def setFieldSetName(f: Field): String = {
      s"_set${upFirst(f.name())}Set"
   }

   def isFieldSet(f: Field, input: String): String = {
      s"_is${upFirst(f.name())}Set($input)"
   }

   def setFieldSet(f: Field, input: String): String = {
      s"${setFieldSetName(f)}($input)"
   }

}