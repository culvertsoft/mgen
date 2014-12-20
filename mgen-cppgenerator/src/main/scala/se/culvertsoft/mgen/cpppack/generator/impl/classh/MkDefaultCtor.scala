package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkDefaultCtor {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    txtBuffer.tabs(1).textln(s"${t.shortName()}();")

  }

}