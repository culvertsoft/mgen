package se.culvertsoft.mgen.javapack.generator

import JavaTypeNames.getTypeName
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum

object JavaConstruction {

  def defaultConstruct(
    typ: Type,
    isGenericArg: Boolean = false)(implicit currentModule: Module): String = {

    typ.typeEnum() match {
      case TypeEnum.BOOL => "false"
      case TypeEnum.INT8 => "(byte)0"
      case TypeEnum.INT16 => "(short)0"
      case TypeEnum.INT32 => "0"
      case TypeEnum.INT64 => "0L"
      case TypeEnum.FLOAT32 => "0.0f"
      case TypeEnum.FLOAT64 => "0.0"
      case TypeEnum.STRING => "\"\""
      case TypeEnum.MAP => s"new ${getTypeName(typ, isGenericArg)}()"
      case TypeEnum.LIST => s"new ${getTypeName(typ, isGenericArg)}()"
      case TypeEnum.ARRAY =>
        val t = typ.asInstanceOf[ArrayType]
        s"new ${getTypeName(t.elementType(), false, true)}[0]"
      case TypeEnum.ENUM => s"${getTypeName(typ)}.UNKNOWN"
      case TypeEnum.CLASS => s"new ${getTypeName(typ, isGenericArg)}()"
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }

  }

  def defaultConstruct(
    field: Field)(implicit currentModule: Module): String = {
    defaultConstruct(field.typ())
  }

  def defaultConstructNull(
    typ: Type,
    isGenericArg: Boolean = false)(implicit currentModule: Module): String = {

    typ.typeEnum() match {
      case TypeEnum.BOOL => "false"
      case TypeEnum.INT8 => "(byte)0"
      case TypeEnum.INT16 => "(short)0"
      case TypeEnum.INT32 => "0"
      case TypeEnum.INT64 => "0L"
      case TypeEnum.FLOAT32 => "0.0f"
      case TypeEnum.FLOAT64 => "0.0"
      case TypeEnum.STRING => "null"
      case TypeEnum.MAP => "null"
      case TypeEnum.LIST => "null"
      case TypeEnum.ARRAY => "null"
      case TypeEnum.CLASS => "null"
      case TypeEnum.ENUM => "null"
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }

  }

  def defaultConstructNull(
    field: Field)(implicit currentModule: Module): String = {
    defaultConstructNull(field.typ())
  }

}