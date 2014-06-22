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

object MkSetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    // By-value-setters with 'const Polymorphic<T>&' in
    for (field <- t.fields()) {
      txtBuffer
        .tabs(0)
        .textln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"const ${getTypeName(field)}& ${field.name()}")} {")
      txtBuffer.tabs(1).textln(s"m_${field.name()} = ${field.name()};")
      txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
      txtBuffer.tabs(1).textln(s"return *this;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

    // By-value-setters with 'const T&' in
    for (field <- t.fields()) {
      if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
        txtBuffer
          .tabs(0)
          .textln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"const ${getTypeName(field.typ(), false)} & ${field.name()}")} {")
        txtBuffer.tabs(1).textln(s"return ${set(field, s"${field.name()}._deepCopy(), true")};")
        txtBuffer.tabs(0).textln(s"}")
        txtBuffer.endl()
      }
    }

    for (field <- t.fields()) {
      if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
        txtBuffer
          .tabs(0)
          .textln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"${getTypeName(field.typ(), false)} * ${field.name()}, const bool managePtr")} {")
        txtBuffer.tabs(1).textln(s"m_${field.name()}.set(${field.name()}, managePtr);")
        txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
        txtBuffer.tabs(1).textln(s"return *this;")
        txtBuffer.tabs(0).textln(s"}")
        txtBuffer.endl()
      }
    }

  }

}