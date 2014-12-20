package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum

object CppTypeNames {

  def getTypeName(typ: Type, isPolymorphicField: Boolean)(implicit referencedFromModule: Module): String = {

    typ.typeEnum() match {
      case TypeEnum.BOOL => "bool"
      case TypeEnum.INT8 => "char"
      case TypeEnum.INT16 => "short"
      case TypeEnum.INT32 => "int"
      case TypeEnum.INT64 => "long long"
      case TypeEnum.FLOAT32 => "float"
      case TypeEnum.FLOAT64 => "double"
      case TypeEnum.STRING => "std::string"
      case TypeEnum.MAP =>
        val t = typ.asInstanceOf[MapType]
        s"std::map<${getTypeName(t.keyType(), isPolymorphicField)}, ${getTypeName(t.valueType(), isPolymorphicField)}> "
      case TypeEnum.ARRAY =>
        val t = typ.asInstanceOf[ArrayType]
        s"std::vector<${getTypeName(t.elementType(), isPolymorphicField)}> "
      case TypeEnum.LIST =>
        val t = typ.asInstanceOf[ListType]
        s"std::vector<${getTypeName(t.elementType(), isPolymorphicField)}> "
      case TypeEnum.CLASS =>
        val t = typ.asInstanceOf[ClassType]
        val name =
          if (t.module() == referencedFromModule) {
            t.shortName()
          } else {
            t.fullName().replaceAllLiterally(".", "::")
          }
        if (isPolymorphicField)
          s"Polymorphic<$name> "
        else
          name
      case TypeEnum.ENUM =>
        val t = typ.asInstanceOf[EnumType]
        if (t.module == referencedFromModule) {
          s"${typ.shortName}"
        } else {
          s"${t.fullName.replaceAllLiterally(".", "::")}"
        }

      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }

  }

  def getTypeName(field: Field)(implicit referencedFromModule: Module): String = {
    getTypeName(field.typ(), field.isPolymorphic())
  }

}