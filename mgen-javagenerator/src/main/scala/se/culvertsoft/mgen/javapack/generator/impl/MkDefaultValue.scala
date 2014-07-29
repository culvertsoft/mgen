package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.StringDefaultValue
import se.culvertsoft.mgen.javapack.generator.JavaConstruction
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames
import se.culvertsoft.mgen.api.model.DefaultValue

import scala.collection.JavaConversions._

object MkDefaultValue {

  def apply(
    f: Field,
    nonNull: Boolean)(implicit currentModule: Module): String = {

    if (!f.hasDefaultValue) {
      if (nonNull)
        return JavaConstruction.defaultConstruct(f.typ, false)
      else
        return JavaConstruction.defaultConstructNull(f.typ, false)
    }

    val d = f.defaultValue()

    apply(d, nonNull, false, true)

  }

  def apply(
    d: DefaultValue,
    nonNull: Boolean,
    isGenericArg: Boolean = false,
    isFirstCall: Boolean = true)(implicit currentModule: Module): String = {

    d match {
      case v: EnumDefaultValue =>
        if (v.isCurrentModule)
          s"${v.expectedType.shortName}.${v.value.name}"
        else {
          s"${v.expectedType.fullName}.${v.value.name}"
        }
      case v: BoolDefaultValue =>
        v.value.toString
      case v: NumericDefaultValue =>
        v.expectedType match {
          case t: Int8Type => s"(byte)${v.fixedPtValue}"
          case t: Int16Type => s"(short)${v.fixedPtValue}"
          case t: Int32Type => s"(int)${v.fixedPtValue}"
          case t: Int64Type => s"(long)${v.fixedPtValue}L"
          case t: Float32Type => s"(float)${v.floatingPtValue}"
          case t: Float64Type => s"(double)${v.floatingPtValue}"
        }
      case v: StringDefaultValue =>
        '"' + v.value + '"'
      case v: ListOrArrayDefaultValue =>
        val values = v.values
        v.expectedType() match {
          case t: ArrayType =>
            val entries = values.map(v => apply(v, true, isGenericArg, false)).mkString(", ")
            s"new ${JavaTypeNames.getTypeName(d.expectedType, isGenericArg, false)} { $entries }"
          case t: ListType =>
            val entries = values.map(v => apply(v, true, true, false)).mkString(", ")
            s"new java.util.ArrayList<${JavaTypeNames.getTypeName(t.elementType(), true, false)}>(java.util.Arrays.asList($entries))"
        }
      case v: MapDefaultValue =>
        throw new GenerationException(s"Not yet implemented!")
      case v: ObjectDefaultValue =>
        throw new GenerationException(s"Not yet implemented!")
      case _ =>
        throw new GenerationException(s"Don't know how to generate default value code for $d")
    }

  }

  /*
 * 
  def getTypeName(
    typ: Type,
    isGenericArg: Boolean = false,
    isArrayCtor: Boolean = false)(implicit currentModule: Module): String = {

    if (typ == null)
      return JavaConstants.mgenBaseClsString

    typ.typeEnum() match {
      case TypeEnum.ENUM =>
        val t = typ.asInstanceOf[EnumType]
        if (t.module == currentModule) {
          t.shortName()
        } else {
          t.fullName()
        }
      case TypeEnum.BOOL => if (isGenericArg) "Boolean" else "boolean"
      case TypeEnum.INT8 => if (isGenericArg) "Byte" else "byte"
      case TypeEnum.INT16 => if (isGenericArg) "Short" else "short"
      case TypeEnum.INT32 => if (isGenericArg) "Integer" else "int"
      case TypeEnum.INT64 => if (isGenericArg) "Long" else "long"
      case TypeEnum.FLOAT32 => if (isGenericArg) "Float" else "float"
      case TypeEnum.FLOAT64 => if (isGenericArg) "Double" else "double"
      case TypeEnum.STRING => "String"
      case TypeEnum.MAP =>
        val t = typ.asInstanceOf[MapType]
        s"java.util.HashMap<${getTypeName(t.keyType(), true)}, ${getTypeName(t.valueType(), true)}>"
      case TypeEnum.LIST =>
        val t = typ.asInstanceOf[ListType]
        s"java.util.ArrayList<${getTypeName(t.elementType(), true)}>"
      case TypeEnum.ARRAY =>
        val t = typ.asInstanceOf[ArrayType]
        if (isArrayCtor)
          s"${getTypeName(t.elementType(), false)}[0]"
        else
          s"${getTypeName(t.elementType(), false)}[]"
      case TypeEnum.CUSTOM =>
        val t = typ.asInstanceOf[CustomType]
        if (t.module() == currentModule) {
          t.name()
        } else {
          t.fullName()
        }
      case TypeEnum.UNKNOWN =>
        throw new GenerationException("Cannot call getTypeName on an UnlinkedCustomType: " + typ.fullName)
    }

  }
 */
}