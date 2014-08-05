package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet

object MkHasers {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields()) {
      ln(s"bool ${t.shortName()}::has${upFirst(field.name)}() const {")
      ln(1, s"return ${isFieldSet(field, "mgen::SHALLOW")};")
      ln("}").endl()
    }

    val allFields = t.fieldsInclSuper()

    for (field <- allFields) {
      val fcnCall = s"unset${upFirst(field.name)}"
      ln(s"${t.shortName}& ${t.shortName}::$fcnCall() {")
      ln(1, s"_set${upFirst(field.name)}Set(false, mgen::SHALLOW);")
      ln(1, s"return *this;")
      ln("}").endl()
    }

  }

}