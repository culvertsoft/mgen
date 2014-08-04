package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

import scala.collection.JavaConversions._

object MkModuleClassRegistry {

  def apply(modules: Seq[Module])(implicit txtBuffer: SuperStringBuffer) = {

    val allTypes = modules.flatMap(_.types).distinct
    txtBuffer {
      ln("blueprint.classes = {};")
      allTypes.foreach({ t =>
        scopeExt("blueprint.classes[\"" + t.fullName + "\"] = ", ";") {
          ln("\"__t\": \"" + t.typeHierarchy().map(x => x.typeId16BitBase64()).mkString("") + "\",")
          for (field <- t.fieldsInclSuper) {
            ln("\"" + field.name + "\": {")
            txtBuffer {
              if(field.hasDefaultValue){
                ln("\"default\": \"" + MkDefaultValue(field.defaultValue()) + "\",")
              }
              ln("\"flags\": [" + field.flags().map(x => "\"" + x + "\"").mkString(",") + "],")
              ln("\"type\": \"" + getTypeName(field.typ()) + "\",")
              ln("\"hash\": \"" + field.idBase64() + "\"")
            }
            if (field == t.fieldsInclSuper.last) ln("}") else ln("},")
          }
        }
      })
    }
  }

  def getTypeName(typ: Type): String = {
    typ match {
      case t: EnumType => "enum:" + t.entries().map(x => x.name() ).mkString(", ")
      case t: BoolType => "boolean"
      case t: Int8Type => "int8"
      case t: Int16Type => "int16"
      case t: Int32Type => "int32"
      case t: Int64Type => "int64"
      case t: Float32Type => "float32"
      case t: Float64Type => "float64"
      case t: StringType => "string"
      case t: MapType => s"map:${getTypeName(t.keyType())}:${getTypeName(t.valueType())}"
      case t: ListOrArrayType => s"list:${getTypeName(t.elementType())}"
      case t: CustomType => t.fullName()
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }
}


