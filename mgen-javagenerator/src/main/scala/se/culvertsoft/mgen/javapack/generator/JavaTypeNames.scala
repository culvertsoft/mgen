package se.culvertsoft.mgen.javapack.generator

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.model.Field

object JavaTypeNames {

  def declared(typ: Type, isGenericArg: Boolean)(implicit currentModule: Module): String = {

    if (typ == null)
      return JavaConstants.mgenBaseClsString

    typ.typeEnum() match {
      case TypeEnum.ENUM =>
        val t = typ.asInstanceOf[EnumType]
        if (t.module == currentModule) {
          t.shortName()
        } else {
          t.fullName()
        }
      case TypeEnum.BOOL    => if (isGenericArg) "Boolean" else "boolean"
      case TypeEnum.INT8    => if (isGenericArg) "Byte" else "byte"
      case TypeEnum.INT16   => if (isGenericArg) "Short" else "short"
      case TypeEnum.INT32   => if (isGenericArg) "Integer" else "int"
      case TypeEnum.INT64   => if (isGenericArg) "Long" else "long"
      case TypeEnum.FLOAT32 => if (isGenericArg) "Float" else "float"
      case TypeEnum.FLOAT64 => if (isGenericArg) "Double" else "double"
      case TypeEnum.STRING  => "String"
      case TypeEnum.MAP =>
        val t = typ.asInstanceOf[MapType]
        s"java.util.Map<${declared(t.keyType, true)}, ${declared(t.valueType, true)}>"
      case TypeEnum.LIST =>
        val t = typ.asInstanceOf[ListType]
        s"java.util.List<${declared(t.elementType, true)}>"
      case TypeEnum.ARRAY =>
        val t = typ.asInstanceOf[ArrayType]
        s"${declared(t.elementType, false)}[]"
      case TypeEnum.CLASS =>
        val t = typ.asInstanceOf[ClassType]
        if (t.module() == currentModule) {
          t.shortName
        } else {
          t.fullName
        }
      case TypeEnum.UNKNOWN =>
        throw new GenerationException("Cannot call getTypeName on an UnlinkedCustomType: " + typ.fullName)
    }

  }

  def declared(field: Field)(implicit currentModule: Module): String = {
    declared(field.typ, false)
  }

}