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
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkLongTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.ListOrArrayType
import se.culvertsoft.mgen.cpppack.generator.CppGenerator

object MkHasers {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields()) {
      ln(s"bool ${t.shortName()}::has${upFirst(field.name)}() const {")
      ln(1, s"return ${isFieldSet(field, "mgen::SHALLOW")};")
      ln("}").endl()
    }

    val allFields = t.getAllFieldsInclSuper()

    for (field <- allFields) {
      val fcnCall = s"unset${upFirst(field.name)}"
      ln(s"${t.shortName}& ${t.shortName}::$fcnCall() {")
      ln(1, s"_set${upFirst(field.name)}Set(false, mgen::SHALLOW);")
      ln(1, s"return *this;")
      ln("}").endl()
    }

  }

}