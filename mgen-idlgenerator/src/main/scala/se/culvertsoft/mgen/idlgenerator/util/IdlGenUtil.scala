package se.culvertsoft.mgen.idlgenerator.util

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap

import org.json.simple.JSONValue

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.BoolDefaultValue
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.EnumDefaultValue
import se.culvertsoft.mgen.api.model.FixedPointType
import se.culvertsoft.mgen.api.model.FloatingPointType
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue
import se.culvertsoft.mgen.api.model.MapDefaultValue
import se.culvertsoft.mgen.api.model.NumericDefaultValue
import se.culvertsoft.mgen.api.model.ObjectDefaultValue
import se.culvertsoft.mgen.api.model.StringDefaultValue

object IdlGenUtil {

  def defaultVal2String(v: DefaultValue): String = {

    if (v == null)
      return null

    v match {
      case v: EnumDefaultValue => getQuotedStringOrNull(v.value.name)
      case v: BoolDefaultValue => getString(v.value)
      case v: StringDefaultValue => getQuotedStringOrNull(JSONValue.escape(v.value))
      case v: NumericDefaultValue =>
        v.expectedType match {
          case _: FixedPointType => getString(v.fixedPtValue)
          case _: FloatingPointType => getString(v.floatingPtValue)
        }
      case v: ListOrArrayDefaultValue => s"[${v.values.map(defaultVal2String).mkString(", ")}]"
      case v: MapDefaultValue =>
        val entries = v.values.map(e => (getQuotedStringOrNull(defaultVal2String(e._1)), defaultVal2String(e._2)))
        val entriesString = entries.map(e => s"${e._1}: ${e._2}").mkString(", ")
        s"{$entriesString}"

      case v: ObjectDefaultValue =>
        val entries = v.overriddenDefaultValues.map(e => (getQuotedStringOrNull(e._1.name), defaultVal2String(e._2)))
        val entriesString = entries.map(e => s"${e._1}: ${e._2}").mkString(", ")

        if (v.isDefaultTypeOverriden) {
          if (v.isLocalDefinition) {
            s"{ ${quote("__TYPE")}: ${quote(v.actualType.shortName)}, $entriesString}"
          } else {
            s"{ ${quote("__TYPE")}: ${quote(v.actualType.fullName)}, $entriesString}"
          }
        } else {
          s"{$entriesString}"
        }

      case _ => throw new GenerationException(s"Don't know how to handle default value $v")
    }
  }

  private def quote(s: String): String = {
    '"' + s + '"'
  }

  private def getQuotedStringOrNull(o: String): String = {

    if (o != null && o.startsWith("\""))
      return o

    if (o != null) ('"' + o.toString + '"') else null
  }

  private def getString(o: Any): String = {
    if (o != null) o.toString else null
  }
}