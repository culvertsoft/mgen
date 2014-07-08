package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import Alias.fieldMetadata
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.modelPkg

object MkFieldMetaData {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fieldsInclSuper()

    if (fields.nonEmpty) {
      for (field <- fields) {

        val flagsString =
          if (field.flags().isEmpty())
            "null"
          else
            s"java.util.Arrays.asList(${field.flags().map(s => '"' + s + '"').mkString(",")})"
        txtBuffer.tabs(1)
          .text(s"public static final ${fieldClsString} ")
          .text(s"${fieldMetadata(field)} = new ${fieldClsString}(")
          .text(quote(t.fullName())).commaSpace()
          .text(quote(field.name())).commaSpace()
          .text(mkMetaData(field.typ())).commaSpace()
          .text(s"$flagsString").commaSpace()
          .text(s"(short)${field.id()});")
          .endl()
      }
      txtBuffer.endl()
    }

    if (fields.nonEmpty) {
      for (field <- fields) {
        txtBuffer.tabs(1).textln(s"public static final short ${fieldId(field)} = (short)${field.id()};")
      }
      txtBuffer.endl()

    }

    ln(1, s"public static final $fieldClsString[] _FIELDS = { ${fields.map(fieldMetadata).mkString(", ")} };")
    txtBuffer.endl()

  }

  private def mkMetaData(t: Type): String = {
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
        s"new ${modelPkg}.impl.UnlinkedCustomType(${quote(tc.fullName())}, ${tc.typeId()}L)"
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }

}