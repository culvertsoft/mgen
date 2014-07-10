package se.culvertsoft.mgen.cpppack.generator

import scala.collection.mutable.HashMap

import CppTypeNames.getTypeName
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum

object CppConstruction {

  val defCtroCache = new HashMap[(Type, Boolean), String]
  val defNullCtroCache = new HashMap[(Type, Boolean), String]

  def defaultConstruct(
    typ: Type,
    isPolymorphicField: Boolean)(implicit currentModule: Module): String = {

    defCtroCache.getOrElseUpdate((typ, isPolymorphicField), {
      typ.typeEnum() match {
        case TypeEnum.BOOL => "false"
        case TypeEnum.INT8 => "(char)0"
        case TypeEnum.INT16 => "(short)0"
        case TypeEnum.INT32 => "0"
        case TypeEnum.INT64 => "0LL"
        case TypeEnum.FLOAT32 => "0.0f"
        case TypeEnum.FLOAT64 => "0.0"
        case TypeEnum.STRING => "\"\""
        case TypeEnum.MAP => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.LIST => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.ARRAY => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.CUSTOM => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.ENUM => s"${typ.shortName()}_UNKNOWN"
        case x => throw new GenerationException(s"Don't know how to handle type $x")
      }
    })

  }

  def defaultConstructNull(
    typ: Type,
    isPolymorphicField: Boolean)(implicit currentModule: Module): String = {

    defNullCtroCache.getOrElseUpdate((typ, isPolymorphicField), {
      typ.typeEnum() match {
        case TypeEnum.MAP => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.LIST => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.ARRAY => s"${getTypeName(typ, isPolymorphicField)}()"
        case TypeEnum.CUSTOM => if (isPolymorphicField) "0" else defaultConstruct(typ, false)
        case TypeEnum.ENUM => s"${typ.shortName()}_UNKNOWN"
        case _ => s"${defaultConstruct(typ, isPolymorphicField)}"
      }
    })

  }

  def defaultConstruct(field: Field)(implicit currentModule: Module): String = {
    defaultConstruct(field.typ(), field.isPolymorphic())
  }

  def defaultConstructNull(field: Field)(implicit currentModule: Module): String = {
    defaultConstructNull(field.typ(), field.isPolymorphic())
  }

}