package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.setFieldSet

object MkSetFieldsSet {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

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