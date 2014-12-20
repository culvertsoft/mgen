package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkDestructor {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    txtBuffer.tabs(0).textln(s"${t.shortName()}::~${t.shortName()}() {")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}