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

      val isSuperField = !t.fields.contains(field)

      ln(s"${t.shortName}& ${t.shortName()}::$fcnCall() {")
      if (isSuperField) {
        ln(1, s"${MkLongTypeName.cpp(t.superType.asInstanceOf[CustomType])}::$fcnCall();")
      } else {
        field.typ() match {
          case t: CustomType if (field.isPolymorphic()) => ln(1, s"m_${field.name}.set(0);")
          case t: MapType => ln(1, s"m_${field.name}.clear();")
          case t: ListOrArrayType => ln(1, s"m_${field.name}.clear();")
          case _ =>
        }
        ln(1, s"${Alias.isSetName(field)} = false;")
      }

      ln(1, s"return *this;")
      ln("}").endl()
    }

  }

}