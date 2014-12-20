package se.culvertsoft.mgen.jspack.generator

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.BoolType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Float32Type
import se.culvertsoft.mgen.api.model.Float64Type
import se.culvertsoft.mgen.api.model.Int16Type
import se.culvertsoft.mgen.api.model.Int32Type
import se.culvertsoft.mgen.api.model.Int64Type
import se.culvertsoft.mgen.api.model.Int8Type
import se.culvertsoft.mgen.api.model.ListOrArrayType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.StringType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.scopeExt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkModuleClassRegistry {

  def apply(modules: Seq[Module])(implicit txtBuffer: SourceCodeBuffer) = {

    val allClasses = modules.flatMap(_.classes)
    txtBuffer {
      ln("blueprint.classes = {};")
      allClasses.foreach({ t =>
        scopeExt("blueprint.classes[\"" + t.fullName + "\"] = ", ";") {
          ln("\"__t\": \"" + t.typeHierarchy().map(x => x.typeId16BitBase64()).mkString("") + "\",")
          for (field <- t.fieldsInclSuper) {
            ln("\"" + field.name + "\": {")
            txtBuffer {
              if(field.hasDefaultValue){
                ln("\"default\": " + MkDefaultValue(field.defaultValue()) + ",")
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
      case t: EnumType => "enum:" + t.entries().map(x => x.name()).mkString(", ")
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
      case t: ClassType => t.fullName()
      case x => throw new GenerationException(s"Don't know how to handle type $x")
    }
  }
}


