package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkNumFieldsSet {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(1).textln(s"int _numFieldsSet(const mgen::FieldSetDepth depth, const bool includeTransient) const;")
    txtBuffer.endl()

  }

}