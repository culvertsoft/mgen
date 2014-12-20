package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkDeepCopy {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    txtBuffer.tabs(0).textln(s"${t.shortName()} * ${t.shortName()}::_deepCopy() const {")
    txtBuffer.tabs(1).textln(s"return new ${t.shortName()}(*this);")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}