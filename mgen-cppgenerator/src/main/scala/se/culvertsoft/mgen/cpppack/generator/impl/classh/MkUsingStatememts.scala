package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

import scala.collection.JavaConversions._

object MkUsingStatements {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    if (t.fields.exists(_.isPolymorphic)) {
      txtBuffer.tabs(0).textln(s"using mgen::Polymorphic;")
      txtBuffer.endl()
    }

  }

}