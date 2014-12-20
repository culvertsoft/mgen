package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkNumFieldsSet {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    txtBuffer.tabs(1).textln(s"int _numFieldsSet(const mgen::FieldSetDepth depth, const bool includeTransient) const;")
    txtBuffer.endl()

  }

}