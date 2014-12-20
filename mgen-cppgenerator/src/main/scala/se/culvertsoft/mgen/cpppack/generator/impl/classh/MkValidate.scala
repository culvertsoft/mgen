package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkValidate {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    txtBuffer.tabs(1).textln(s"bool _validate(const mgen::FieldSetDepth depth) const;").endl()

  }

}