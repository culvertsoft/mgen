package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldIdString
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.get
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkIsFieldSet {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    ln(s"bool ${t.shortName()}::_isFieldSet(const mgen::Field& field, const mgen::FieldSetDepth depth) const {")
    ln(1, s"switch(field.id()) {")
    for (field <- t.fieldsInclSuper()) {
      ln(2, s"case (${fieldIdString(field)}):")
      ln(3, s"return ${isFieldSet(field, "depth")};")
    }
    ln(2, s"default:")
    ln(3, s"return false;")
    ln(1, s"}")
    ln(s"}")
    endl()

    for (field <- t.fields()) {
      ln(s"bool ${t.shortName()}::${isFieldSet(field, "const mgen::FieldSetDepth depth")} const {")
      if (field.typ().containsUserDefinedType()) {

        val shallowCall = if (CppGenerator.canBeNull(field)) s"m_${field.name()}.get() != 0" else isSetName(field)

        ln(1, s"if (depth == mgen::SHALLOW) {")
        ln(2, s"return $shallowCall;")
        ln(1, s"} else {")
        ln(2, s"return $shallowCall && mgen::validation::validateFieldDeep(${get(field)});")
        ln(1, s"}")
      } else {
        ln(1, s"return ${isSetName(field)};")
      }
      ln(s"}")
      endl()
    }

  }

}