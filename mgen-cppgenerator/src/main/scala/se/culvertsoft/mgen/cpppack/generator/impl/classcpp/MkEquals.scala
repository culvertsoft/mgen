package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkEquals {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::_equals(const mgen::MGenBase& other) const {")
    txtBuffer.tabs(1).textln(s"return _type_id == other._typeId() && static_cast<const ${t.shortName()}&>(other) == *this;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}