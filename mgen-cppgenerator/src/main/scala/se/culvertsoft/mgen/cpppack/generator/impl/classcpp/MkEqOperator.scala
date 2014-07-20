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

object MkEqOperator {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.fieldsInclSuper()
    ln(s"bool ${t.shortName()}::operator==(const ${t.shortName()}& other) const {")
    ln(1, "return true")
    for (field <- allFields) {
      ln(2, s" && ${isFieldSet(field, "mgen::SHALLOW")} == other.${isFieldSet(field, "mgen::SHALLOW")}")
    }
    for (field <- allFields) {
      txt(2, s" && ${get(field)} == other.${get(field)}")
      if (field != allFields.last)
        endl()
    }
    ln(";")
    ln(s"}")
    endl()

    ln(s"bool ${t.shortName()}::operator!=(const ${t.shortName()}& other) const {")
    ln(1, "return !(*this == other);")
    ln(s"}")
    endl()

  }

}