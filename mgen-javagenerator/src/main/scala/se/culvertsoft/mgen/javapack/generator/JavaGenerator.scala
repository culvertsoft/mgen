package se.culvertsoft.mgen.javapack.generator

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.seqAsJavaList
import JavaConstants._
import JavaConstruction.defaultConstruct
import JavaConstruction.defaultConstructNull
import JavaReadCalls.mkReadCall
import JavaTypeNames.getTypeName
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.internal.BuiltInJavaCppGenerator
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator.getModuleFolderPath
import java.io.File

class JavaGenerator extends BuiltInJavaCppGenerator {
  import JavaConstants._
  import JavaTypeNames._
  import JavaConstruction._
  import JavaToString._
  import JavaReadCalls._

  val txtBuffer = new SuperStringBuffer

  override def generateTopLevelMetaSources(
    folder: String,
    packagePath: String,
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val fileName = "MGenClassRegistry" + ".java"
    val sourceCode = generateTopLevelClassRegistrySourceCode(referencedModules, packagePath)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateModuleMetaSources(
    module: Module,
    generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = "MGenModuleClassRegistry" + ".java"
    val sourceCode = generateClassRegistrySourceCode()
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  override def generateClassSources(module: Module, t: CustomType, generatorSettings: java.util.Map[String, String]): java.util.Collection[GeneratedSourceFile] = {
    val folder = getModuleFolderPath(module, generatorSettings)
    val fileName = t.name() + ".java"
    val sourceCode = generateClassSourceCode(t)
    List(new GeneratedSourceFile(folder + File.separator + fileName, sourceCode))
  }

  def generateTopLevelClassRegistrySourceCode(referencedModules: Seq[Module], packagePath: String): String = {

    txtBuffer.clear()

    mkPackage(packagePath)

    mkClassStart("MGenClassRegistry", clsRegistryClsString)

    txtBuffer.tabs(1).textln("public MGenClassRegistry() {")
    for (module <- referencedModules) {
      val fullRegistryClassName = s"${module.path()}.MGenModuleClassRegistry"
      txtBuffer.tabs(2).textln(s"add(new ${fullRegistryClassName}());")
    }
    txtBuffer.tabs(1).textln("}")

    mkClassEnd()

    txtBuffer.toString()
  }

  def generateClassRegistrySourceCode(): String = {

    txtBuffer.clear()

    mkPackage(currentModule.path())

    txtBuffer.textln("import se.culvertsoft.mgen.javapack.classes.Ctor;")
    txtBuffer.textln("import se.culvertsoft.mgen.javapack.classes.MGenBase;")
    txtBuffer.endl()

    mkClassStart("MGenModuleClassRegistry", clsRegistryClsString)

    txtBuffer.tabs(1).textln("public MGenModuleClassRegistry() {")
    for ((_, typ) <- currentModule.types()) {
      txtBuffer.tabs(2).textln(s"add(")
      txtBuffer.tabs(3).textln(s"${quote(typ.fullName())},")
      txtBuffer.tabs(3).textln(s"(short)${typ.typeHash16bit()},")
      txtBuffer.tabs(3).textln(s"(int)${typ.typeHash32bit()},")
      txtBuffer.tabs(3).textln(s"${typ.fullName()}.class,")
      txtBuffer.tabs(3).textln(s"new Ctor() { public MGenBase create() { return new ${typ.fullName()}(); } }")
      txtBuffer.tabs(2).textln(s");")
    }
    txtBuffer.tabs(1).textln("}")

    mkClassEnd()

    txtBuffer.toString()
  }

  def generateClassSourceCode(t: CustomType): String = {

    txtBuffer.clear()

    mkFancyHeader(t)

    mkPackage(currentModule.path())
    mkImports(t)
    mkClassStart(t)
    mkMembers(t)
    mkDefaultCtor(t)
    mkRequiredMembersCtor(t)
    mkAllMembersAsArgsCtor(t)
    mkGetters(t)
    mkSetters(t)
    mkToString(t)
    mkHashCode(t)
    mkEquals(t)
    mkDeepCopy(t)

    mkMetadataMethodsComment(t)

    mkTypeName(t)
    mkTypeHashes(t)
    mkAcceptVisitor(t)
    mkreadField(t)
    mkGetFields(t)
    mkIsFieldSet(t)
    mkMarkFieldsSet(t)
    mkValidate(t)
    mkNFieldsSet(t)
    mkFieldBy16BitHash(t)
    mkFieldBy32BitHash(t)
    mkTypeHierarchyMethods(t)

    mkMetadataComment(t)

    mkMetaDataFields(t)
    mkTypeHierarchyFields(t)
    mkMetaDataFieldsStatic(t)
    mkTypeHierarchyStatic(t)
    mkClassEnd()

    txtBuffer.toString()
  }

  def mkToString(t: CustomType) {

    val fields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("@Override")

    txtBuffer.tabs(1).textln("public String toString() {")

    if (fields.nonEmpty) {
      txtBuffer.tabs(2).textln("final java.lang.StringBuffer sb = new java.lang.StringBuffer();")

      txtBuffer.tabs(2).text("sb.append(\"").text(t.fullName()).textln(":\\n\");")

      for ((field, i) <- fields.zipWithIndex) {
        txtBuffer.tabs(2)
          .text("sb.append(\"  \")")
          .text(".append(\"")
          .text(field.name())
          .text(" = \")")
          .text(s".append(${JavaToString.mkToString(field.typ())(s"${get(field)}")})")
        if (i + 1 < fields.size())
          txtBuffer.text(".append(\"\\n\");")
        else
          txtBuffer.text(";")
        txtBuffer.endl()
      }
      txtBuffer.tabs(2).textln("return sb.toString();")
    } else {
      txtBuffer.tabs(2).textln("return _typeName() + \"_instance\";")
    }

    txtBuffer.tabs(1).textln("}").endl()

  }

  def mkEquals(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public boolean equals(final Object other) {")
    txtBuffer.tabs(2).textln(s"if (other == null) return false;")
    txtBuffer.tabs(2).textln(s"if (other == this) return true;")
    txtBuffer.tabs(2).textln(s"if (${t.name()}.class != other.getClass()) return false;")

    if (allFields.nonEmpty)
      txtBuffer.tabs(2).textln(s"final ${t.name()} o = (${t.name()})other;")
    txtBuffer.tabs(2).text("return true")
    for (field <- allFields) {
      txtBuffer.endl().tabs(2).text(s"  && (${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")} == o.${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")})")
    }
    for (field <- allFields) {
      txtBuffer.endl().tabs(2).text(s"  && ${eqTesterClsString}.areEqual(${get(field)}, o.${get(field)}, ${meta(field)}.typ())")
    }
    txtBuffer.textln(";")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkDeepCopy(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public ${t.shortName()} deepCopy() {")
    txtBuffer.tabs(2).textln(s"final ${t.shortName()} out = new ${t.shortName()}();")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"out.${set(field, s"${deepCopyerClsString}.deepCopy(${get(field)}, ${meta(field)}.typ())")};")
    for (field <- allFields) {
      val shallow = s"${fieldSetDepthClsString}.SHALLOW"
      val isFieldSetString = isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")
      txtBuffer.tabs(2).textln(s"out.${setFieldSet(field, s"$isFieldSetString, $shallow")};")
    }
    txtBuffer.tabs(2).textln("return out;")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkPackage(packagePath: String) {
    txtBuffer.text(s"package ${packagePath};").endl2()
  }

  def mkImports(t: CustomType) {
    val allReferencedExtTypes = t.getAllReferencedExtTypesInclSuper()
    txtBuffer.textln(s"import ${fieldClsStringQ};")
    for (referencedExtType <- allReferencedExtTypes)
      txtBuffer.textln(s"import ${referencedExtType.module().path()};")
    //txtBuffer.textln(s"import ${fieldDefValueClsStringQ};")
    txtBuffer.textln(s"import ${fieldSetDepthClsStringQ};")
    txtBuffer.textln(s"import ${fieldVisitorClsStringQ};")
    txtBuffer.textln(s"import ${readerClsStringQ};")
    if (t.getAllFieldsInclSuper().nonEmpty) {
      txtBuffer.textln(s"import ${eqTesterClsStringQ};")
      txtBuffer.textln(s"import ${deepCopyerClsStringQ};")
      txtBuffer.textln(s"import ${fieldHasherClsStringQ};")
    }
    if (t.fields().exists(_.typ().containsMgenCreatedType())) {
      txtBuffer.textln(s"import ${validatorClsStringQ};")
      txtBuffer.textln(s"import ${setFieldSetClsStringQ};")
    }
    txtBuffer.endl()
  }

  def mkClassStart(clsName: String, superTypeName: String) {
    txtBuffer.text(s"public class $clsName extends $superTypeName {").endl2()
  }

  def mkClassStart(t: CustomType) {
    mkClassStart(t.name(), getTypeName(t.superType()))
  }

  def mkDefaultCtor(t: CustomType) {
    /*
      txtBuffer.tabs(1).textln(s"public ${t.name()}() {")
      txtBuffer.tabs(2).textln(s"this(${fieldDefValueClsString}.NULL);");
      txtBuffer.tabs(1).textln("}")
      txtBuffer.endl()
*/
    txtBuffer.tabs(1).textln(s"public ${t.name()}() {")
    txtBuffer.tabs(2).textln(s"super();");
    for (field <- t.fields()) {
      txtBuffer.tabs(2).textln(s"m_${field.name()} = ${defaultConstructNull(field.typ())};")
    }
    for (field <- t.fields()) {
      txtBuffer.tabs(2).textln(s"${isSetName(field)} = false;")
    }
    txtBuffer.tabs(1).textln("}")
    txtBuffer.endl()

  }

  def mkRequiredMembersCtor(t: CustomType) {
    val reqAndOptFields = t.getAllFieldsInclSuper().toBuffer
    val reqFields = t.getAllFieldsInclSuper().filter(_.isRequired())
    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txtBuffer.tabs(1).text(s"public ${t.name()}(")
      for (i <- 0 until reqFields.size()) {
        val field = reqFields.get(i)
        val isLastField = i + 1 == reqFields.size()
        txtBuffer.tabs(if (i > 0) 4 else 0).text(s"final ${fieldTypeName(field)} ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(") {")

      val fieldsToSuper = reqFields -- t.fields
      if (fieldsToSuper.nonEmpty) {
        txtBuffer.tabs(2).text("super(")
        for (i <- 0 until fieldsToSuper.size()) {
          val field = fieldsToSuper.get(i)
          val isLastField = i + 1 == fieldsToSuper.size()
          txtBuffer.text(field.name())
          if (!isLastField) {
            txtBuffer.text(", ")
          }
        }
        txtBuffer.textln(");")
      }

      val ownFields = reqFields -- fieldsToSuper
      for (field <- ownFields)
        txtBuffer.tabs(2).textln(s"m_${field.name()} = ${field.name()};")
      for (field <- ownFields)
        txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
      for (field <- (t.fields() -- ownFields))
        txtBuffer.tabs(2).textln(s"${isSetName(field)} = false;")
      txtBuffer.tabs(1).textln("}").endl()
    }
  }

  def mkAllMembersAsArgsCtor(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()
    if (allFields.nonEmpty) {
      txtBuffer.tabs(1).text(s"public ${t.name()}(")
      for (i <- 0 until allFields.size()) {
        val field = allFields.get(i)
        val isLastField = i + 1 == allFields.size()
        txtBuffer.tabs(if (i > 0) 4 else 0).text(s"final ${fieldTypeName(field)} ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(") {")

      val fieldsToSuper = allFields -- t.fields
      if (fieldsToSuper.nonEmpty) {
        txtBuffer.tabs(2).text("super(")
        for (i <- 0 until fieldsToSuper.size()) {
          val field = fieldsToSuper.get(i)
          val isLastField = i + 1 == fieldsToSuper.size()
          txtBuffer.text(field.name())
          if (!isLastField) {
            txtBuffer.text(", ")
          }
        }
        txtBuffer.textln(");")
      }

      for (field <- t.fields())
        txtBuffer.tabs(2).textln(s"m_${field.name()} = ${field.name()};")
      for (field <- t.fields())
        txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")

      txtBuffer.tabs(1).textln("}").endl()
    }
  }

  def mkGetters(t: CustomType) {
    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public ${getTypeName(field.typ())} ${get(field)} {")
      txtBuffer.tabs(2).textln(s"return m_${field.name()};")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    for (field <- t.fields()) {
      if (!field.typ().isSimple()) {
        txtBuffer.tabs(1).textln(s"public ${getTypeName(field.typ())} ${get(field, "Mutable")} {")
        txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
        txtBuffer.tabs(2).textln(s"return m_${field.name()};")
        txtBuffer.tabs(1).textln(s"}").endl()
      }
    }

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public boolean has${upFirst(field.name())}() {")
      txtBuffer.tabs(2).textln(s"return ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")};")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    for (field <- t.getAllFieldsInclSuper()) {
      txtBuffer.tabs(1).textln(s"public ${t.shortName()} unset${upFirst(field.name())}() {")
      txtBuffer.tabs(2).textln(s"_set${upFirst(field.name())}Set(false, ${fieldSetDepthClsString}.SHALLOW);")
      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}").endl()
    }
  }

  def mkSetters(t: CustomType) {

    val thisFields = t.fields()
    val superFields = t.getAllFieldsInclSuper() -- thisFields

    for (field <- thisFields) {
      txtBuffer.tabs(1).textln(s"public ${t.name()} ${set(field, s"final ${getTypeName(field.typ())} ${field.name()}")} {")
      txtBuffer.tabs(2).textln(s"m_${field.name()} = ${field.name()};")
      txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    for (field <- superFields) {
      txtBuffer.tabs(1).textln(s"public ${t.name()} ${set(field, s"final ${getTypeName(field.typ())} ${field.name()}")} {")
      txtBuffer.tabs(2).textln(s"super.${set(field, field.name())};")
      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    /*
      for (field <- t.fields()) {
         txtBuffer.tabs(1).textln(s"public ${getTypeName(field.typ())} ${set(field, "")} {")
         txtBuffer.tabs(2).textln(s"${setFieldSet(field, s"true, ${fieldSetDepthClsString}.SHALLOW")};")
         txtBuffer.tabs(2).textln(s"return ${get(field)};")
         txtBuffer.tabs(1).textln(s"}").endl()
      }*/
  }

  def mkHashCode(t: CustomType) {

    val allFields = t.getAllFieldsInclSuper()
    val hashBase = t.fullName().hashCode()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public int hashCode() {")

    if (allFields.nonEmpty) {
      txtBuffer.tabs(2).textln(s"final int prime = 31;")
      txtBuffer.tabs(2).textln(s"int result = ${hashBase};")
      for (f <- allFields) {
        txtBuffer.tabs(2).textln(s"result = ${isFieldSet(f, s"${fieldSetDepthClsString}.SHALLOW")} ? (prime * result + ${fieldHasherClsString}.calc(${get(f)}, ${meta(f)}.typ())) : result;")
      }
      txtBuffer.tabs(2).textln(s"return result;")
    } else {
      txtBuffer.tabs(2).textln(s"return ${hashBase};")
    }

    txtBuffer.tabs(1).textln("}").endl()

  }

  def mkTypeName(t: CustomType) {
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public String _typeName() {")
    txtBuffer.tabs(2).textln("return _TYPE_NAME;")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkTypeHashes(t: CustomType) {

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public short _typeHash16bit() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_HASH_16BIT;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public int _typeHash32bit() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_HASH_32BIT;")
    txtBuffer.tabs(1).textln("}").endl()

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

  def mkMembers(t: CustomType) {
    val fields = t.fields()
    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"private ${getTypeName(field.typ())} m_${field.name()};")
    }

    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"private boolean ${isSetName(field)};")
    }

    if (fields.nonEmpty)
      txtBuffer.endl()
  }

  def mkIsFieldSet(t: CustomType) {

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public boolean ${isFieldSet(field, s"final ${fieldSetDepthClsString} fieldSetDepth")} {")
      if (field.typ().containsMgenCreatedType()) {
        txtBuffer.tabs(2).textln(s"if (fieldSetDepth == ${fieldSetDepthClsString}.SHALLOW) {")
        txtBuffer.tabs(3).textln(s"return ${isSetName(field)};")
        txtBuffer.tabs(2).textln(s"} else {")
        txtBuffer.tabs(3).textln(s"return ${isSetName(field)} && ${validatorClsString}.validateFieldDeep(${get(field)}, ${meta(field)}.typ());")
        txtBuffer.tabs(2).textln(s"}")
      } else {
        txtBuffer.tabs(2).textln(s"return ${isSetName(field)};")
      }
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    txtBuffer.tabs(1).textln(s"public boolean _isFieldSet(final $fieldClsString field, final ${fieldSetDepthClsString} depth) {")
    txtBuffer.tabs(2).textln(s"switch(field.fieldHash16bit()) {")
    for (field <- t.getAllFieldsInclSuper()) {
      txtBuffer.tabs(3).textln(s"case (${hash16(field)}):")
      txtBuffer.tabs(4).textln(s"return ${isFieldSet(field, "depth")};")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"return false;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln(s"}").endl()
  }

  def mkMarkFieldsSet(t: CustomType) {

    val fields = t.fields()
    val allFields = t.getAllFieldsInclSuper()

    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"public ${t.shortName()} ${setFieldSet(field, s"final boolean state, final ${fieldSetDepthClsString} depth")} {")
      txtBuffer.tabs(2).textln(s"${isSetName(field)} = state;")

      if (field.typ().containsMgenCreatedType()) {
        txtBuffer.tabs(2).textln(s"if (depth == ${fieldSetDepthClsString}.DEEP)")
        txtBuffer.tabs(3).textln(s"${setFieldSetClsString}.setFieldSetDeep(${get(field)}, ${meta(field)}.typ());")
      }

      txtBuffer.tabs(2).textln(s"if (!state)")
      txtBuffer.tabs(3).textln(s"m_${field.name()} = ${defaultConstructNull(field.typ())};")

      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()
    }

    txtBuffer.tabs(1).textln(s"public ${t.shortName()} _setAllFieldsSet(final boolean state, final ${fieldSetDepthClsString} depth) { ")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"${setFieldSet(field, "state, depth")};")
    txtBuffer.tabs(2).textln(s"return this;")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()

  }

  def mkValidate(t: CustomType) {
    txtBuffer.tabs(1).textln(s"public boolean _validate(final ${fieldSetDepthClsString} fieldSetDepth) { ")
    txtBuffer.tabs(2).textln(s"if (fieldSetDepth == ${fieldSetDepthClsString}.SHALLOW) {")
    txtBuffer.tabs(3).text(s"return true")
    for (field <- t.getAllFieldsInclSuper().filter(_.isRequired()))
      txtBuffer.endl().tabs(4).text(s"&& ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")}")
    txtBuffer.textln(s";")
    txtBuffer.tabs(2).textln(s"} else {")
    txtBuffer.tabs(3).text(s"return true")
    for (field <- t.getAllFieldsInclSuper()) {
      if (field.isRequired())
        txtBuffer.endl().tabs(4).text(s"&& ${isFieldSet(field, s"${fieldSetDepthClsString}.DEEP")}")
      else if (field.typ().containsMgenCreatedType())
        txtBuffer.endl().tabs(4).text(s"&& (!${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")} || ${isFieldSet(field, s"${fieldSetDepthClsString}.DEEP")})")
    }
    txtBuffer.textln(s";")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()
  }

  def mkMetaDataFields(t: CustomType) {

    val fields = t.getAllFieldsInclSuper()

    if (fields.nonEmpty) {
      for (field <- fields) {

        val flagsString =
          if (field.flags().isEmpty())
            "null"
          else
            s"java.util.Arrays.asList(${field.flags().map(s => '"' + s + '"').mkString(",")})"
        txtBuffer.tabs(1)
          .text(s"public static final ${fieldClsString} ")
          .text(s"${meta(field)} = new ${fieldClsString}(")
          .text(quote(t.fullName())).commaSpace()
          .text(quote(field.name())).commaSpace()
          .text(mkMetaData(field.typ())).commaSpace()
          .text(s"$flagsString);")
          .endl()
      }
      txtBuffer.endl()
    }

    if (fields.nonEmpty) {
      for (field <- fields) {
        txtBuffer.tabs(1).textln(s"public static final short ${hash16(field)} = ${field.fieldHash16bit()};")
      }
      txtBuffer.endl()
    }

    if (fields.nonEmpty) {
      for (field <- fields) {
        txtBuffer.tabs(1).textln(s"public static final int ${hash32(field)} = ${field.fieldHash32bit()};")
      }
      txtBuffer.endl()
    }

    txtBuffer.tabs(1).textln(s"public static final String _TYPE_NAME = ${quote(t.fullName())};");
    txtBuffer.tabs(1).textln(s"public static final short _TYPE_HASH_16BIT = ${t.typeHash16bit()};");
    txtBuffer.tabs(1).textln(s"public static final int _TYPE_HASH_32BIT = ${t.typeHash32bit()};");
    txtBuffer.endl()

    txtBuffer.tabs(1).textln(s"public static final $colClsString<$fieldClsString> FIELDS;");
    txtBuffer.endl()

  }

  def quote(s: String): String = {
    '"' + s + '"'
  }

  def mkMetaData(t: Type): String = {
    t.typeEnum() match {
      case TypeEnum.BOOL => s"${modelPkg}.BoolType.INSTANCE"
      case TypeEnum.INT8 => s"${modelPkg}.Int8Type.INSTANCE"
      case TypeEnum.INT16 => s"${modelPkg}.Int16Type.INSTANCE"
      case TypeEnum.INT32 => s"${modelPkg}.Int32Type.INSTANCE"
      case TypeEnum.INT64 => s"${modelPkg}.Int64Type.INSTANCE"
      case TypeEnum.FLOAT32 => s"${modelPkg}.Float32Type.INSTANCE"
      case TypeEnum.FLOAT64 => s"${modelPkg}.Float64Type.INSTANCE"
      case TypeEnum.STRING => s"${modelPkg}.StringType.INSTANCE"
      case TypeEnum.MAP =>
        val tm = t.asInstanceOf[MapType]
        s"new ${modelPkg}.impl.MapTypeImpl(${mkMetaData(tm.keyType())}, ${mkMetaData(tm.valueType())})"
      case TypeEnum.LIST =>
        val tl = t.asInstanceOf[ListType]
        s"new ${modelPkg}.impl.ListTypeImpl(${mkMetaData(tl.elementType())})"
      case TypeEnum.ARRAY =>
        val ta = t.asInstanceOf[ArrayType]
        s"new ${modelPkg}.impl.ArrayTypeImpl(${mkMetaData(ta.elementType())})"
      case TypeEnum.CUSTOM =>
        val tc = t.asInstanceOf[CustomType]
        s"new ${modelPkg}.impl.UnknownCustomTypeImpl(${quote(tc.fullName())})"
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }

  def mkClassEnd() {
    txtBuffer.text("}").endl()
  }

  def mkNFieldsSet(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public int _nFieldsSet(final ${fieldSetDepthClsString} fieldSetDepth) {")
    txtBuffer.tabs(2).textln(s"int out = 0;")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"out += ${isFieldSet(field, "fieldSetDepth")} ? 1 : 0;")
    txtBuffer.tabs(2).textln(s"return out;")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkGetFields(t: CustomType) {
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public $colClsString<$fieldClsString> _fields() {")
    txtBuffer.tabs(2).textln(s"return FIELDS;")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkFieldBy16BitHash(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public $fieldClsString _fieldBy16BitHash(final short hash) {")
    txtBuffer.tabs(2).textln(s"switch(hash) {")
    for (field <- allFields) {
      txtBuffer.tabs(3).textln(s"case (${hash16(field)}):")
      txtBuffer.tabs(4).textln(s"return ${meta(field)};")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"return null;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkFieldBy32BitHash(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public $fieldClsString _fieldBy32BitHash(final int hash) {")
    txtBuffer.tabs(2).textln(s"switch(hash) {")
    for (field <- allFields) {
      txtBuffer.tabs(3).textln(s"case (${hash32(field)}):")
      txtBuffer.tabs(4).textln(s"return ${meta(field)};")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"return null;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkAcceptVisitor(t: CustomType) {
    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public void _accept(final ${fieldVisitorClsString} visitor) throws java.io.IOException {")
    txtBuffer.tabs(2).textln(s"visitor.beginVisit(this, _nFieldsSet(${fieldSetDepthClsString}.SHALLOW));")
    for (field <- allFields) {
      txtBuffer.tabs(2).textln(s"visitor.visit(${get(field)}, ${meta(field)}, ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")});")
    }
    txtBuffer.tabs(2).textln(s"visitor.endVisit();")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkreadField(t: CustomType) {

    val allFields = t.getAllFieldsInclSuper()
    val needsSupress = allFields.map(_.typ().typeEnum()).find(e => e == TypeEnum.LIST || e == TypeEnum.MAP).isDefined

    if (needsSupress)
      txtBuffer.tabs(1).textln("@SuppressWarnings(\"unchecked\")")
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public boolean _readField(final $fieldClsString field,")
    txtBuffer.tabs(1).textln(s"                         final Object context,")
    txtBuffer.tabs(1).textln(s"                         final $readerClsString reader) throws java.io.IOException {")
    txtBuffer.tabs(2).textln(s"switch(field.fieldHash16bit()) {")
    for (field <- allFields) {
      txtBuffer.tabs(3).textln(s"case (${hash16(field)}):")
      txtBuffer.tabs(4).textln(s"set${upFirst(field.name())}((${getTypeName(field.typ())})reader.${mkReadCall(field)}(field, context));")
      txtBuffer.tabs(4).textln("return true;")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"reader.handleUnknownField(field, context);")
    txtBuffer.tabs(4).textln(s"return false;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkTypeHierarchyMethods(t: CustomType) {

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public short[] _typeHashes16bit() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_HASHES_16BIT;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public int[] _typeHashes32bit() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_HASHES_32BIT;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public java.util.Collection<String> _typeNames() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_NAMES;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public java.util.Collection<String> _typeHashes16bitBase64() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_HASHES_16BIT_BASE64;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public java.util.Collection<String> _typeHashes32bitBase64() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_HASHES_32BIT_BASE64;")
    txtBuffer.tabs(1).textln("}").endl()

  }

  def mkMetaDataFieldsStatic(t: CustomType) {

    val fields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("static {")
    txtBuffer.tabs(2).textln(s"final $arrayListClsString<$fieldClsString> fields = new $arrayListClsString<$fieldClsString>();")
    for (field <- fields) {
      txtBuffer.tabs(2).textln(s"fields.add(${meta(field)});")
    }
    txtBuffer.tabs(2).textln(s"FIELDS = fields;")
    txtBuffer.tabs(1).textln("}")
    txtBuffer.endl()
  }

  def mkTypeHierarchyStatic(t: CustomType) {

    txtBuffer.tabs(1).textln("static {")
    txtBuffer.tabs(2).textln(s"_TYPE_HASHES_16BIT = new short[${t.typeHierarchy().size()}];")
    txtBuffer.tabs(2).textln(s"_TYPE_HASHES_32BIT = new int[${t.typeHierarchy().size()}];")
    txtBuffer.tabs(2).textln(s"final java.util.ArrayList<String> names = new java.util.ArrayList<String>();")
    txtBuffer.tabs(2).textln(s"final java.util.ArrayList<String> base6416bit = new java.util.ArrayList<String>();")
    txtBuffer.tabs(2).textln(s"final java.util.ArrayList<String> base6432bit = new java.util.ArrayList<String>();")
    for ((ht, i) <- t.typeHierarchy().zipWithIndex) {
      txtBuffer.tabs(2).textln(s"_TYPE_HASHES_16BIT[$i] = ${ht.typeHash16bit()};")
      txtBuffer.tabs(2).textln(s"_TYPE_HASHES_32BIT[$i] = ${ht.typeHash32bit()};")
      txtBuffer.tabs(2).textln(s"names.add(${quote(ht.fullName())});")
      txtBuffer.tabs(2).textln(s"base6416bit.add(${quote(ht.typeHash16bitBase64String())});")
      txtBuffer.tabs(2).textln(s"base6432bit.add(${quote(ht.typeHash32bitBase64String())});")
    }
    txtBuffer.tabs(2).textln(s"_TYPE_NAMES = names;")
    txtBuffer.tabs(2).textln(s"_TYPE_HASHES_16BIT_BASE64 = base6416bit;")
    txtBuffer.tabs(2).textln(s"_TYPE_HASHES_32BIT_BASE64 = base6432bit;")
    txtBuffer.tabs(1).textln("}").endl()
  }

  def mkTypeHierarchyFields(t: CustomType) {
    txtBuffer.tabs(1).textln("public static final short[] _TYPE_HASHES_16BIT;")
    txtBuffer.tabs(1).textln("public static final int[] _TYPE_HASHES_32BIT;")
    txtBuffer.tabs(1).textln("public static final java.util.Collection<String> _TYPE_NAMES;")
    txtBuffer.tabs(1).textln("public static final java.util.Collection<String> _TYPE_HASHES_16BIT_BASE64;")
    txtBuffer.tabs(1).textln("public static final java.util.Collection<String> _TYPE_HASHES_32BIT_BASE64;").endl()
  }

  def mkMetadataMethodsComment(t: CustomType) {
    txtBuffer.textln(serializationSectionHeader).endl();
  }

  def mkMetadataComment(t: CustomType) {
    txtBuffer.textln(metadataSectionHeader).endl();
  }

  def mkFancyHeader(t: CustomType) = {
    txtBuffer.textln(fileHeader);
  }

  def fieldTypeName(field: Field): String = {
    getTypeName(field.typ())
  }

  def get(field: Field, preParan: String = ""): String = {
    s"get${upFirst(field.name())}${preParan}()"
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

  def meta(field: Field): String = {
    s"_${field.name()}_METADATA"
  }

}