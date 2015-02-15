package se.culvertsoft.mgen.javapack.generator

import JavaTypeNames.declared
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum

object JavaConstruction {

  def defaultConstruct(typ: Type)(implicit currentModule: Module): String = {

    typ.typeEnum() match {
      case TypeEnum.BOOL    => "false"
      case TypeEnum.INT8    => "(byte)0"
      case TypeEnum.INT16   => "(short)0"
      case TypeEnum.INT32   => "0"
      case TypeEnum.INT64   => "0L"
      case TypeEnum.FLOAT32 => "0.0f"
      case TypeEnum.FLOAT64 => "0.0"
      case TypeEnum.STRING  => "\"\""
      case TypeEnum.MAP =>
        val t = typ.asInstanceOf[MapType]
        s"new java.util.HashMap<${declared(t.keyType, true)}, ${declared(t.valueType, true)}>()"
      case TypeEnum.LIST =>
        val t = typ.asInstanceOf[ListType]
        s"new java.util.ArrayList<${declared(t.elementType, true)}>()"
      case TypeEnum.ARRAY =>
        val t = typ.asInstanceOf[ArrayType]
        val needsZeros = s"new ${declared(t, false)}"
        addZerosToArraySuffixBrackets(needsZeros)
      case TypeEnum.ENUM  => s"${declared(typ, false)}.UNKNOWN"
      case TypeEnum.CLASS => s"new ${declared(typ, false)}()"
      case x              => throw new GenerationException(s"Don't know how to handle type $x")
    }

  }

  private def addZerosToArraySuffixBrackets(needsZeros: String): String = {
    val iStart = needsZeros.lastIndexWhere(c => c != '[' && c != ']') + 1
    val (pre, post) = needsZeros.splitAt(iStart)
    pre + post.replaceAllLiterally("[]", "[0]")
  }

  def defaultConstruct(field: Field)(implicit currentModule: Module): String = {
    defaultConstruct(field.typ)
  }

  def defaultConstructNull(typ: Type)(implicit currentModule: Module): String = {

    typ.typeEnum() match {
      case TypeEnum.BOOL    => "false"
      case TypeEnum.INT8    => "(byte)0"
      case TypeEnum.INT16   => "(short)0"
      case TypeEnum.INT32   => "0"
      case TypeEnum.INT64   => "0L"
      case TypeEnum.FLOAT32 => "0.0f"
      case TypeEnum.FLOAT64 => "0.0"
      case TypeEnum.STRING  => "null"
      case TypeEnum.MAP     => "null"
      case TypeEnum.LIST    => "null"
      case TypeEnum.ARRAY   => "null"
      case TypeEnum.CLASS   => "null"
      case TypeEnum.ENUM    => "null"
      case x                => throw new GenerationException(s"Don't know how to handle type $x")
    }

  }

  def defaultConstructNull(field: Field)(implicit currentModule: Module): String = {
    defaultConstructNull(field.typ)
  }

}