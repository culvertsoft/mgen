package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
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
import se.culvertsoft.mgen.api.model.Field

object MkDefaultValue {

  def apply(
    f: Field,
    isGenericArg: Boolean = false)(implicit currentModule: Module): String = {

    if (!f.hasDefaultValue)
      return JavaConstruction.defaultConstructNull(f.typ, isGenericArg)

    f.defaultValue match {
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
        throw new GenerationException(s"Not yet implemented!")
      case v: MapDefaultValue =>
        throw new GenerationException(s"Not yet implemented!")
      case v: ObjectDefaultValue =>
        throw new GenerationException(s"Not yet implemented!")
      case _ =>
        throw new GenerationException(s"Don't know how to generate default value code for $f")
    }

  }

}