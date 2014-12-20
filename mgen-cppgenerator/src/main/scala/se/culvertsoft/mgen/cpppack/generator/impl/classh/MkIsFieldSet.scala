package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet

object MkIsFieldSet {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"bool ${isFieldSet(field, "const mgen::FieldSetDepth depth")} const;")
    }

    if (t.fields().nonEmpty)
      txtBuffer.endl()

  }

}