package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._

object MkModuleClassRegistry {

  def apply(modules: Seq[Module])(implicit txtBuffer: SuperStringBuffer) = {

    val allTypes = modules.flatMap(_.types.values).distinct
    txtBuffer {
      ln("registry.classRegistry = {};")
      allTypes.foreach({ t =>
        scope("registry.classRegistry[\"" + t.fullName + "\"] = ") {
          ln("\"__t\": \"" + t.superTypeHierarchy().map(x => x.typeId16BitBase64()).mkString("") + "\",")
          for(field <-  t.getAllFieldsInclSuper){
           ln("\"" + field.name + "\": {")
           txtBuffer {
              ln("\"flags\": [" + field.flags().map(x => "\"" + x + "\"").mkString(",") + "],")
              ln("\"type\": \"" + getTypeName(field.typ()) + "\",")
              ln("\"hash\": \"" + field.idBase64() + "\"")
            }
            if (field == t.getAllFieldsInclSuper.last) ln("}") else ln("},")
          }
        }
      })
    }
  }

  def getTypeName(typ: Type): String = {
    typ.typeEnum() match {
      case TypeEnum.BOOL => "boolean"
      case TypeEnum.INT8 => "int8"
      case TypeEnum.INT16 => "int16"
      case TypeEnum.INT32 => "int32"
      case TypeEnum.INT64 => "int64"
      case TypeEnum.FLOAT32 => "float32"
      case TypeEnum.FLOAT64 => "float64"
      case TypeEnum.STRING => "string"
      case TypeEnum.MAP =>
        val t = typ.asInstanceOf[MapType]
        s"map:${getTypeName(t.keyType())}:${getTypeName(t.valueType())}"
      case TypeEnum.LIST | TypeEnum.ARRAY =>
        val t = typ.asInstanceOf[ListOrArrayType]
        s"list:${getTypeName(t.elementType())}"
      case TypeEnum.CUSTOM =>
        val t = typ.asInstanceOf[CustomType]
        t.fullName()
      case TypeEnum.MGEN_BASE => MGenBaseType.INSTANCE.fullName()
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }
}


