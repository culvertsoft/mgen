package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import Alias.fieldMetadata
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldIfcClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.modelPkg
import se.culvertsoft.mgen.javapack.generator.JavaConstants.runtimeClassClsStringQ

object MkFieldMetaData {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val fields = t.fieldsInclSuper()

    if (fields.nonEmpty) {
      for (field <- fields) {

        val flagsString =
          if (field.flags().isEmpty())
            "null"
          else
            s"java.util.Arrays.asList(${field.flags().map(s => '"' + s + '"').mkString(",")})"
        txt(1, s"public static final ${fieldIfcClsString} ")
          .text(s"${fieldMetadata(field)} = new ${fieldIfcClsString}(")
          .text(quote(t.fullName())).commaSpace()
          .text(quote(field.name())).commaSpace()
          .text(mkMetaData(field.typ())).commaSpace()
          .text(s"$flagsString").commaSpace()
          .text(s"(short)${field.id()});")
          .endl()
      }
      ln()
    }

    if (fields.nonEmpty) {
      for (field <- fields) {
        ln(1, s"public static final short ${fieldId(field)} = (short)${field.id()};")
      }
      ln()

    }

    ln(1, s"public static final $fieldIfcClsString[] _FIELDS = { ${fields.map(fieldMetadata).mkString(", ")} };")
    ln()

  }

  private def mkMetaData(t: Type): String = {
    t.typeEnum() match {
      case TypeEnum.BOOL    => s"${modelPkg}.BoolType.INSTANCE"
      case TypeEnum.INT8    => s"${modelPkg}.Int8Type.INSTANCE"
      case TypeEnum.INT16   => s"${modelPkg}.Int16Type.INSTANCE"
      case TypeEnum.INT32   => s"${modelPkg}.Int32Type.INSTANCE"
      case TypeEnum.INT64   => s"${modelPkg}.Int64Type.INSTANCE"
      case TypeEnum.FLOAT32 => s"${modelPkg}.Float32Type.INSTANCE"
      case TypeEnum.FLOAT64 => s"${modelPkg}.Float64Type.INSTANCE"
      case TypeEnum.STRING  => s"${modelPkg}.StringType.INSTANCE"
      case TypeEnum.MAP =>
        val tm = t.asInstanceOf[MapType]
        s"new ${modelPkg}.MapType(${mkMetaData(tm.keyType())}, ${mkMetaData(tm.valueType())})"
      case TypeEnum.LIST =>
        val tl = t.asInstanceOf[ListType]
        s"new ${modelPkg}.ListType(${mkMetaData(tl.elementType())})"
      case TypeEnum.ARRAY =>
        val ta = t.asInstanceOf[ArrayType]
        s"new ${modelPkg}.ArrayType(${mkMetaData(ta.elementType())})"
      case TypeEnum.CLASS =>
        val tc = t.asInstanceOf[ClassType]
        s"new ${runtimeClassClsStringQ}(${quote(tc.fullName())}, ${tc.typeId()}L)"
      case TypeEnum.ENUM => s"${t.fullName}._TYPE"
      case x             => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }

}