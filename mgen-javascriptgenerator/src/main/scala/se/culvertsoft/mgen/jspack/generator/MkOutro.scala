package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

import scala.collection.JavaConversions._

object MkOutro {
  // this variable name can be changed with the setting: classregistry_name
  def apply(settings: java.util.Map[String, String])(implicit txtBuffer: SourceCodeBuffer) = {
    val classregistryBlueprint = settings.getOrElse("classregistryblueprint", "mgen_registry")

    ln("")
    ln("")
    txtBuffer {
      ln("// Support for requirejs etc.")
      ln("if (typeof define === \"function\" && define.amd) {")
      txtBuffer {
        ln("define(\"" + classregistryBlueprint + "\", [], function(){")
        txtBuffer {
          ln(s"return blueprint;")
        }
        ln("});")
      }
      ln("} else {")
      txtBuffer {
        ln("// expose blueprint the old fashioned way.")
        ln(s"window.$classregistryBlueprint = blueprint;")
      }
      ln("}")
    }

    ln("})();")
  }
}