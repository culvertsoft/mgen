package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet

object MkIsFieldSet {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"bool ${isFieldSet(field, "const mgen::FieldSetDepth depth")} const;")
    }

    if (t.fields().nonEmpty)
      txtBuffer.endl()

  }

}