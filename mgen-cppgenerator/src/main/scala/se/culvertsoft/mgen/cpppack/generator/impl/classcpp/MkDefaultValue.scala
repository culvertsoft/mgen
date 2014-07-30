package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.StringDefaultValue
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.ArrayType
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.impl.Alias
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames

object MkDefaultValue {

  def apply(f: Field)(implicit currentModule: Module): String = {

    if (!f.hasDefaultValue) {
      return CppConstruction.defaultConstructNull(f.typ, f.isPolymorphic)
    }

    val d = f.defaultValue()

    apply(d, f.isPolymorphic)

  }

  def apply(
    d: DefaultValue,
    isPolymorphicField: Boolean)(implicit currentModule: Module): String = {

    d match {
      case v: EnumDefaultValue =>
        if (v.isCurrentModule)
          s"${v.expectedType.shortName}_${v.value.name}"
        else {
          s"${v.expectedType.module.path.replaceAllLiterally(".", "::")}::${v.expectedType.shortName}_${v.value.name}"
        }
      case v: BoolDefaultValue =>
        v.value.toString
      case v: NumericDefaultValue =>
        v.expectedType match {
          case t: Int8Type => s"(char)${v.fixedPtValue}"
          case t: Int16Type => s"(short)${v.fixedPtValue}"
          case t: Int32Type => s"${v.fixedPtValue}"
          case t: Int64Type => s"${v.fixedPtValue}LL"
          case t: Float32Type => s"(float)${v.floatingPtValue}"
          case t: Float64Type => s"${v.floatingPtValue}"
        }
      case v: StringDefaultValue =>
        '"' + v.value + '"'
      case v: ListOrArrayDefaultValue =>
        val values = v.values
        val typeArg = s"${CppTypeNames.getTypeName(v.expectedType.elementType, isPolymorphicField)}"
        if (values.nonEmpty) {
          val entries = values.map(v => s".add(${apply(v, isPolymorphicField)})").mkString("")
          s"mgen::make_vector<$typeArg>(${values.size})${entries}.make()"
        } else {
          s"std::vector<$typeArg>()"
        }
      case v: MapDefaultValue =>
        val t = v.expectedType
        val typeArg = s"${CppTypeNames.getTypeName(t.keyType, isPolymorphicField)}, ${CppTypeNames.getTypeName(t.valueType, isPolymorphicField)}"
        if (v.values.nonEmpty) {
          val values = v.values.map(e => s".put(${apply(e._1, isPolymorphicField)}, ${apply(e._2, isPolymorphicField)})").mkString("")
          s"mgen::make_map<$typeArg>()${values}.make()"
        } else {
          s"std::map<$typeArg>()"
        }
      case v: ObjectDefaultValue =>
        val values = v.overriddenDefaultValues
        if (values.nonEmpty) {
          val setCalls = values.map(e => s"${Alias.set(e._1, apply(e._2, isPolymorphicField))}").mkString(".")
          val tName = if (v.isCurrentModule) v.actualType.shortName else v.actualType.fullName
          if (isPolymorphicField) {
            s"&((${CppConstruction.defaultConstruct(v.actualType, isPolymorphicField)})->${setCalls})"
          } else {
            CppConstruction.defaultConstruct(v.actualType, isPolymorphicField) + "." + setCalls
          }

        } else {
          CppConstruction.defaultConstruct(v.actualType, isPolymorphicField)
        }
      case _ =>
        throw new GenerationException(s"Don't know how to generate default value code for $d")
    }

  }
}