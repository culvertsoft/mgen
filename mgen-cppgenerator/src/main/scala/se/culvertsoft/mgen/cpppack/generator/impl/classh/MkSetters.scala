package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.set

object MkSetters {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val module = t.module

    val thisFields = t.fields().toSeq
    val superFields = t.fieldsInclSuper() -- thisFields

    val superString = CppGenUtils.getSuperTypeString(t)

    for (coll <- List(superFields, thisFields)) {

      for (field <- coll) {

        // By-value-setters with 'const Polymorphic<T>&' in
        ln(1, s"${t.shortName()}& ${set(field, s"const ${getTypeName(field)}& ${field.name()}")};")

        // By-value-setters with 'const T&' in
        if (field.typ.isInstanceOf[ClassType] && field.isPolymorphic()) {
          ln(1, s"${t.shortName()}& ${set(field, s"const ${getTypeName(field.typ(), false)} & ${field.name()}")};")
        }

        // By-reference-setters with 'T *' in
        if (field.typ.isInstanceOf[ClassType] && field.isPolymorphic()) {
          ln(1, s"${t.shortName()}& ${set(field, s"${getTypeName(field.typ(), false)} * ${field.name()}, const bool managePtr = true")};")
        }

      }
    }

    if (t.fields().nonEmpty)
      endl()

  }

}