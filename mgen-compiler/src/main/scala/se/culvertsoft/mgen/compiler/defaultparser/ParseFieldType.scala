package se.culvertsoft.mgen.compiler.defaultparser

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.impl.ArrayTypeImpl
import se.culvertsoft.mgen.api.model.impl.BoolTypeImpl
import se.culvertsoft.mgen.api.model.impl.Float32TypeImpl
import se.culvertsoft.mgen.api.model.impl.Float64TypeImpl
import se.culvertsoft.mgen.api.model.impl.Int16TypeImpl
import se.culvertsoft.mgen.api.model.impl.Int32TypeImpl
import se.culvertsoft.mgen.api.model.impl.Int64TypeImpl
import se.culvertsoft.mgen.api.model.impl.Int8TypeImpl
import se.culvertsoft.mgen.api.model.impl.ListTypeImpl
import se.culvertsoft.mgen.api.model.impl.MapTypeImpl
import se.culvertsoft.mgen.api.model.impl.StringTypeImpl
import se.culvertsoft.mgen.api.model.impl.TypeImpl
import se.culvertsoft.mgen.api.model.impl.UnlinkedCustomType

object ParseFieldType {

  def apply(untrimmed: String): TypeImpl = {

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
        case "bool" => BoolTypeImpl.INSTANCE
        case "byte" | "int8" => Int8TypeImpl.INSTANCE
        case "int16" => Int16TypeImpl.INSTANCE
        case "int32" | "int" => Int32TypeImpl.INSTANCE
        case "int64" => Int64TypeImpl.INSTANCE
        case "float32" | "float" => Float32TypeImpl.INSTANCE
        case "float64" | "double" => Float64TypeImpl.INSTANCE
        case "string" => StringTypeImpl.INSTANCE
        case _ =>

          if (lcTypeString.startsWith("list[")) {

            val elmTypeString = withinBraces(typeString)
            new ListTypeImpl(ParseFieldType(elmTypeString))

          } else if (lcTypeString.startsWith("array[")) {

            val elmTypeString = withinBraces(typeString)
            new ArrayTypeImpl(ParseFieldType(elmTypeString))

          } else if (lcTypeString.startsWith("map[")) {

            val pairTypeString = withinBraces(typeString)

            val items = pairTypeString.split(',')
            val keyTypeString = items.head
            val valueTypeString = items.tail.mkString(", ")

            new MapTypeImpl(ParseFieldType(keyTypeString), ParseFieldType(valueTypeString))

          } else {
            new UnlinkedCustomType(typeString, -1)
          }

      }
    } catch {
      case e: Exception =>
        throw new AnalysisException(s"ERROR: Failed to parse field type from '$untrimmed'", e)
    }

  }
}