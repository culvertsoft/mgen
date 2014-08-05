package se.culvertsoft.mgen.jspack.generator

import java.io.File

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model._

import scala.collection.JavaConversions._

object MkDefaultValue {

  def apply(v: DefaultValue): String = {

    if (v == null)
      return null

    v match {
      case v: EnumDefaultValue => getQuotedStringOrNull(v.value.name)
      case v: BoolDefaultValue => getString(v.value)
      case v: StringDefaultValue => getQuotedStringOrNull(v.value)
      case v: NumericDefaultValue =>
        v.expectedType match {
          case _: Int8Type => getString(v.fixedPtValue)
          case _: Int16Type => getString(v.fixedPtValue)
          case _: Int32Type => getString(v.fixedPtValue)
          case _: Int64Type => getString(v.fixedPtValue)
          case _: Float32Type => getString(v.floatingPtValue)
          case _: Float64Type => getString(v.floatingPtValue)
        }
      case v: ListOrArrayDefaultValue => s"[${v.values.map(apply).mkString(", ")}]"
      case v: MapDefaultValue =>
        val entries = v.values.map(e => (getQuotedStringOrNull(apply(e._1)), apply(e._2)))
        val entriesString = entries.map(e => s"${e._1}: ${e._2}").mkString(", ")
        s"{$entriesString}"

      case v: ObjectDefaultValue =>
        val defaultDefaultValues = v.actualType().fields().map(x => (x, x.defaultValue())).toMap
        val overridenDefaultValues = v.overriddenDefaultValues()

        val allDefaultValues = defaultDefaultValues ++ overridenDefaultValues
        val entries = allDefaultValues.map(e => (getQuotedStringOrNull(e._1.name), apply(e._2)))
        val entriesString = entries.map(e => s"${e._1}: ${e._2}").mkString(", ")

        if (v.isDefaultTypeOverriden) {
            s"{ ${quote("__t")}: ${quote(v.actualType.typeId16BitBase64Hierarchy())}, $entriesString}"
        } else {
          s"{$entriesString}"
        }

      case _ => throw new GenerationException(s"Don't know how to handle default vallsue $v")
    }
  }

  def quote(s: String): String = {
    '"' + s + '"'
  }

  def getQuotedStringOrNull(o: String): String = {

    if (o != null && o.startsWith("\""))
      return o

    if (o != null) ('"' + o.toString + '"') else null
  }

  def getString(o: Any): String = {
    if (o != null) o.toString else null
  }
}