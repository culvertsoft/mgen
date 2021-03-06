package se.culvertsoft.mgen.javapack.generator

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.model.ArrayType

object JavaToString {

  def mkToString(t: Type): (String => String) = {
    membName: String =>
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
        case TypeEnum.ARRAY =>
          if (JavaGenerator.canBeNull(t.asInstanceOf[ArrayType].elementType))
            s"java.util.Arrays.deepToString($membName)"
          else
            s"java.util.Arrays.toString($membName)"
        case TypeEnum.ENUM => membName
        case TypeEnum.CLASS => membName
        case x => throw new GenerationException(s"Don't know how to handle type $x")
      }
  }

}