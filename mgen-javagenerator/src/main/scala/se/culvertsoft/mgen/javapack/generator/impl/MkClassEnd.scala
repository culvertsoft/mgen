package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkClassEnd {

  def apply()(implicit txtBuffer: SourceCodeBuffer) {
    ln("}")
  }
}