package se.culvertsoft.mgen.idlparser

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.UnlinkedType

object ParseFieldType {

  def apply(untrimmed: String): Type = {

    def withinBraces(container: String): String = {
      val start = container.indexOf('[') + 1
      val end = container.lastIndexOf(']')
      val contents = container.substring(start, end).trim()
      contents
    }

    val typeString = untrimmed.trim()
    val lcTypeString = typeString.toLowerCase()

    try {
      lcTypeString match {
        case "bool" => BoolType.INSTANCE
        case "byte" | "int8" => Int8Type.INSTANCE
        case "int16" => Int16Type.INSTANCE
        case "int32" | "int" => Int32Type.INSTANCE
        case "int64" => Int64Type.INSTANCE
        case "float32" | "float" => Float32Type.INSTANCE
        case "float64" | "double" => Float64Type.INSTANCE
        case "string" => StringType.INSTANCE
        case _ =>

          if (lcTypeString.startsWith("list[")) {

            val elmTypeString = withinBraces(typeString)
            new ListType(ParseFieldType(elmTypeString))

          } else if (lcTypeString.startsWith("array[")) {

            val elmTypeString = withinBraces(typeString)
            new ArrayType(ParseFieldType(elmTypeString))

          } else if (lcTypeString.startsWith("map[")) {

            val pairTypeString = withinBraces(typeString)

            val items = pairTypeString.split(',')
            val keyTypeString = items.head
            val valueTypeString = items.tail.mkString(", ")

            new MapType(ParseFieldType(keyTypeString), ParseFieldType(valueTypeString))

          } else {
            new UnlinkedType(typeString)
          }

      }
    } catch {
      case e: Exception =>
        throw new AnalysisException(s"ERROR: Failed to parse field type from '$untrimmed'", e)
    }

  }
}