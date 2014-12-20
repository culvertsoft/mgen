package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkNFieldsSet {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()
    val nonTransientFields = allFields.filterNot(_.isTransient)
    val haveTransientFields = allFields.size != nonTransientFields.size

    ln(1, "@Override")
    ln(1, s"public int _nFieldsSet(final ${fieldSetDepthClsString} depth, final boolean includeTransient) {")
    ln(2, s"int out = 0;")

    if (haveTransientFields) {
      ln(3, s"if (includeTransient) {")
      for (field <- allFields)
        ln(4, s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
      ln(3, s"} else {")
      for (field <- nonTransientFields)
        ln(4, s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
      ln(3, s"}")
    } else {
      for (field <- allFields)
        ln(2, s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
    }

    ln(2, s"return out;")
    ln(1, "}").endl()

  }
}