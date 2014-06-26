package se.culvertsoft.mgen.jspack.generator

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkHashSwitch {

  def apply(types: Seq[CustomType], depth: Int = 0)(implicit txtBuffer: SuperStringBuffer) {
    scope("switch( t[" + depth + "] ) ") {
      for (t <- types) {
        ln("case \"" + t.typeId16BitBase64() + "\":")
        txtBuffer {
          if (t.subTypes().nonEmpty) {
            apply(t.subTypes(), depth + 1)
          }
          ln("return registry.classRegistry[\"" + t.fullName() + "\"];")
        }
      }
    }
  }
}