package se.culvertsoft.mgen.jspack.generator

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.scope
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkHashSwitch {

  def apply(types: Seq[ClassType], depth: Int = 0)(implicit txtBuffer: SourceCodeBuffer) {
    scope("switch( t[" + depth + "] ) ") {
      for (t <- types) {
        ln("case \"" + t.typeId16BitBase64() + "\":")
        txtBuffer {
          if (t.subTypes().nonEmpty) {
            apply(t.subTypes(), depth + 1)
          }
          ln("return \"" + t.fullName() + "\";")
        }
      }
    }
  }
}