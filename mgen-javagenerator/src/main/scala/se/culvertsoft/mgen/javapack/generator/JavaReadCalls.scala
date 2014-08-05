package se.culvertsoft.mgen.javapack.generator

import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum

object JavaReadCalls {

  def mkReadCall(field: Field): String = {
    field.typ().typeEnum() match {
      case TypeEnum.BOOL => "readBooleanField"
      case TypeEnum.INT8 => "readInt8Field"
      case TypeEnum.INT16 => "readInt16Field"
      case TypeEnum.INT32 => "readInt32Field"
      case TypeEnum.INT64 => "readInt64Field"
      case TypeEnum.FLOAT32 => "readFloat32Field"
      case TypeEnum.FLOAT64 => "readFloat64Field"
      case TypeEnum.STRING => "readStringField"
      case TypeEnum.MAP => "readMapField"
      case TypeEnum.LIST => "readListField"
      case TypeEnum.ARRAY => "readArrayField"
      case TypeEnum.CLASS => "readMgenObjectField"
      case TypeEnum.ENUM => "readEnumField"
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }

}