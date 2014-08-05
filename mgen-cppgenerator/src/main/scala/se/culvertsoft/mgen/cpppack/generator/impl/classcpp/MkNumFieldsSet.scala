package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet

object MkNumFieldsSet {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.fieldsInclSuper()
    val nonTransientFields = allFields.filterNot(_.isTransient)
    val haveTransientFields = allFields.size != nonTransientFields.size

    ln(0, s"int ${t.shortName()}::_numFieldsSet(const mgen::FieldSetDepth depth, const bool includeTransient) const {")
    ln(1, s"int out = 0;")

    if (haveTransientFields) {
      ln(1, s"if (includeTransient) {")
      for (field <- allFields)
        ln(2, s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
      ln(1, s"} else {")
      for (field <- nonTransientFields)
        ln(2, s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
      ln(1, s"}")
    } else {
      for (field <- allFields)
        ln(1, s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
    }
    ln(1, s"return out;")
    ln(0, "}").endl()

  }

}