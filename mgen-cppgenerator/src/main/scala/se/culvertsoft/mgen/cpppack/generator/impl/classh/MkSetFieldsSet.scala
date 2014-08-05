package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.setFieldSet

object MkSetFieldsSet {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val fields = t.fields()
    val allFields = t.fieldsInclSuper()

    for (field <- fields)
      txtBuffer.tabs(1).textln(s"${t.shortName()}& ${setFieldSet(field, "const bool state, const mgen::FieldSetDepth depth")};")
    if (fields.nonEmpty)
      txtBuffer.endl()

    txtBuffer.tabs(1).textln(s"${t.shortName()}& _setAllFieldsSet(const bool state, const mgen::FieldSetDepth depth);")
    txtBuffer.endl()

  }

}