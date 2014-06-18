package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkClassEnd {

  def apply()(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.text("}").endl()
  }
}