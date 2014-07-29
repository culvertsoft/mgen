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
import se.culvertsoft.mgen.javapack.generator.JavaConstants

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
            val typeArg = s"${JavaTypeNames.getTypeName(t.elementType(), true, false)}"
            if (values.nonEmpty) {
              val entries = values.map(v => apply(v, true, false, false)).mkString("add(", ").add(", ").make()")
              s"new ${JavaConstants.listMakerClsStringQ}<$typeArg>(${values.size}).${entries}"
            } else {
              s"new java.util.ArrayList<$typeArg>()"
            }
        }
      case v: MapDefaultValue =>
        val values = v.values.map(e => s"${apply(e._1, true, isGenericArg, false)}, ${apply(e._2, true, isGenericArg, false)}")
        val t = v.expectedType
        val typeArg = s"${JavaTypeNames.getTypeName(t.keyType, true, false)}, ${JavaTypeNames.getTypeName(t.valueType, true, false)}"
        if (values.nonEmpty) {
          val entries = values.mkString("put(", ").put(", ").make()")
          s"new ${JavaConstants.mapMakerClsStringQ}<$typeArg>(${values.size}).${entries}"
        } else {
          s"new java.util.HashMap<$typeArg>()"
        }
      case v: ObjectDefaultValue =>
        val values = v.overriddenDefaultValues
        val setCalls = values.map(e => s".${Alias.set(e._1, apply(e._2, true, isGenericArg, false))}").mkString("")
        JavaConstruction.defaultConstruct(v.actualType, false) + setCalls
      case _ =>
        throw new GenerationException(s"Don't know how to generate default value code for $d")
    }

  }

}