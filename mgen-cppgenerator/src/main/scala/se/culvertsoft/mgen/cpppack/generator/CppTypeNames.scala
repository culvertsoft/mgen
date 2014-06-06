package se.culvertsoft.mgen.cpppack.generator

import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.types.Field
import se.culvertsoft.mgen.api.types.Module
import se.culvertsoft.mgen.api.types.ArrayType
import se.culvertsoft.mgen.api.types.CustomType
import se.culvertsoft.mgen.api.types.ListType
import se.culvertsoft.mgen.api.types.MGenBaseType
import se.culvertsoft.mgen.api.types.MapType
import se.culvertsoft.mgen.api.types.Type
import se.culvertsoft.mgen.api.types.TypeEnum

object CppTypeNames {

   val typeStringCache = new HashMap[(Type, Boolean), String]

   def getTypeName(
      typ: Type,
      isPolymorphicField: Boolean)(implicit currentModule: Module): String = {
      typeStringCache.getOrElseUpdate((typ, isPolymorphicField), {
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
            case TypeEnum.CUSTOM =>
               val t = typ.asInstanceOf[CustomType]
               val name =
                  if (t.module() == currentModule) {
                     t.name()
                  } else {
                     t.fullName()
                  }
               if (isPolymorphicField)
                  s"Polymorphic<$name> "
               else
                  name
            case TypeEnum.MGEN_BASE => MGenBaseType.INSTANCE.fullName()
            case x => throw new GenerationException(s"Don't know how to handle type $x")
         }
      })
   }

   def getTypeName(field: Field)(implicit currentModule: Module): String = {
      getTypeName(field.typ(), field.isPolymorphic())
   }

}