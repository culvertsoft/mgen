package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.set

object MkSetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields()) {

      // By-value-setters with 'const Polymorphic<T>&' in
      txtBuffer
        .tabs(1)
        .textln(s"${t.shortName()}& ${set(field, s"const ${getTypeName(field)}& ${field.name()}")};")

      // By-value-setters with 'const T&' in
      if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
        txtBuffer
          .tabs(1)
          .textln(s"${t.shortName()}& ${set(field, s"const ${getTypeName(field.typ(), false)} & ${field.name()}")};")
      }

      // By-reference-setters with 'T *' in
      if (field.typ().isMGenCreatedType() && field.isPolymorphic()) {
        txtBuffer
          .tabs(1)
          .textln(s"${t.shortName()}& ${set(field, s"${getTypeName(field.typ(), false)} * ${field.name()}, const bool managePtr = true")};")
      }

    }

    if (t.fields().nonEmpty)
      txtBuffer.endl()

  }

}