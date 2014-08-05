package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkEqOperator {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(1).textln(s"bool operator==(const ${t.shortName()}& other) const;")
    txtBuffer.tabs(1).textln(s"bool operator!=(const ${t.shortName()}& other) const;")
    txtBuffer.endl()

  }

}