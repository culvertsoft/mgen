package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.StringDefaultValue
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.javapack.generator.JavaConstruction
import se.culvertsoft.mgen.javapack.generator.JavaConstruction.defaultConstruct
import se.culvertsoft.mgen.javapack.generator.JavaConstruction.defaultConstructNull
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.declared

object MkDefaultValue {

  def apply(f: Field, nonNull: Boolean)(implicit currentModule: Module): String = {

    if (!f.hasDefaultValue) {
      if (nonNull)
        return defaultConstruct(f.typ)
      else
        return defaultConstructNull(f.typ)
    }

    val d = f.defaultValue()

    apply(d, nonNull, false, true)

  }

  def apply(
    d: DefaultValue,
    nonNull: Boolean,
    isGenericArg: Boolean = false,
    isFirstCall: Boolean = true,
    lastIsArray: Boolean = false)(implicit currentModule: Module): String = {

    d match {
      case v: EnumDefaultValue =>
        if (v.isLocalDefinition)
          s"${v.expectedType.shortName}.${v.value.name}"
        else {
          s"${v.expectedType.fullName}.${v.value.name}"
        }
      case v: BoolDefaultValue =>
        v.value.toString
      case v: NumericDefaultValue =>
        v.expectedType match {
          case t: Int8Type    => s"(byte)${v.fixedPtValue}"
          case t: Int16Type   => s"(short)${v.fixedPtValue}"
          case t: Int32Type   => s"${v.fixedPtValue}"
          case t: Int64Type   => s"${v.fixedPtValue}L"
          case t: Float32Type => s"(float)${v.floatingPtValue}"
          case t: Float64Type => s"${v.floatingPtValue}"
        }
      case v: StringDefaultValue =>
        '"' + escape(v.value) + '"'
      case v: ListOrArrayDefaultValue =>
        val values = v.values
        v.expectedType() match {
          case t: ArrayType =>
            val entries = values.map(v => apply(v, true, isGenericArg, false, true)).mkString(", ")
            val prepend =
              if (!lastIsArray)
                s"new ${declared(d.expectedType, false)}"
              else
                ""
            s"${prepend}{$entries}"
          case t: ListType =>
            val typeArg = s"${declared(t.elementType, true)}"
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
        val typeArg = s"${declared(t.keyType, true)}, ${declared(t.valueType, true)}"
        if (values.nonEmpty) {
          val entries = values.mkString("put(", ").put(", ").make()")
          s"new ${JavaConstants.mapMakerClsStringQ}<$typeArg>(${values.size}).${entries}"
        } else {
          s"new java.util.HashMap<$typeArg>()"
        }
      case v: ObjectDefaultValue =>
        val values = v.overriddenDefaultValues
        val setCalls = values.map(e => s".${Alias.set(e._1, apply(e._2, true, isGenericArg, false))}").mkString("")
        JavaConstruction.defaultConstruct(v.actualType) + setCalls
      case _ =>
        throw new GenerationException(s"Don't know how to generate default value code for $d")
    }

  }

  def escape(value: String): String = {
    value
      .replaceAllLiterally("\\", "\\\\")
      .replaceAllLiterally("\"", "\\\"")
      .replaceAllLiterally("\n", "\\\n")
      .replaceAllLiterally("\r", "\\\r")
  }

}