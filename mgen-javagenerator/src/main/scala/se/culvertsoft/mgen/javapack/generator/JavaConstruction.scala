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
      case TypeEnum.MAP     => constructMap(typ.asInstanceOf[MapType], "16")
      case TypeEnum.LIST    => constructList(typ.asInstanceOf[ListType], "16")
      case TypeEnum.ARRAY   => constructArray(typ.asInstanceOf[ArrayType], "0")
      case TypeEnum.ENUM    => s"${declared(typ, false)}.UNKNOWN"
      case TypeEnum.CLASS   => s"new ${declared(typ, false)}()"
      case x                => throw new GenerationException(s"Don't know how to handle type $x")
    }

  }

  def constructList(t: ListType, n: String)(implicit currentModule: Module): String = {
    s"new java.util.ArrayList<${declared(t.elementType, true)}>($n)"
  }

  def constructMap(t: MapType, n: String)(implicit currentModule: Module): String = {
    s"new java.util.HashMap<${declared(t.keyType, true)}, ${declared(t.valueType, true)}>($n)"
  }

  def constructArray(t: ArrayType, n: String)(implicit currentModule: Module): String = {
    val needsSz = s"new ${declared(t, false)}"
    addIntToArraySuffixBrackets(needsSz, n)
  }

  private def addIntToArraySuffixBrackets(needsZeros: String, n: String = "0"): String = {
    val iStart = needsZeros.lastIndexWhere(c => c != '[' && c != ']') + 1
    val (pre, post) = needsZeros.splitAt(iStart)
    pre + post.replaceAllLiterally("[]", s"[$n]")
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