package se.culvertsoft.mgen.javapack.generator

import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum

object JavaToString {

  val toStringCache = new HashMap[Type, (String => String)]

  def mkToString(t: Type): (String => String) = {
    toStringCache.getOrElseUpdate(t, { membName =>
      t.typeEnum() match {
        case TypeEnum.BOOL => membName
        case TypeEnum.INT8 => membName
        case TypeEnum.INT16 => membName
        case TypeEnum.INT32 => membName
        case TypeEnum.INT64 => membName
        case TypeEnum.FLOAT32 => membName
        case TypeEnum.FLOAT64 => membName
        case TypeEnum.STRING => membName
        case TypeEnum.MAP => membName
        case TypeEnum.LIST => membName
        case TypeEnum.ARRAY => s"java.util.Arrays.deepToString($membName)"
        case TypeEnum.CUSTOM => membName
        case x => throw new GenerationException(s"Don't know how to handle type $x")
      }
    })
  }

}