package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkNewInstance {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(0).textln(s"mgen::MGenBase * ${t.shortName()}::_newInstance() {")
    txtBuffer.tabs(1).textln(s"return new ${t.shortName()};")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}