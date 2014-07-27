package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._

object MkNumFieldsSet {

  def apply(
    t: CustomType,
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