package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.set
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkSetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module
    val thisFields = t.fields().toSeq
    val superFields = t.fieldsInclSuper() -- thisFields

    val superString = CppGenUtils.getSuperTypeString(t)

    for (coll <- List(superFields, thisFields)) {

      val toSuper = coll eq superFields

      // By-value-setters with 'const Polymorphic<T>&' in
      for (field <- coll) {
        ln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"const ${getTypeName(field)}& ${field.name()}")} {")
        if (toSuper) {
          ln(1, s"$superString::${set(field, s"${field.name()}")};")
        } else {
          ln(1, s"m_${field.name()} = ${field.name()};")
          if (!CppGenerator.canBeNull(field))
            ln(1, s"${isSetName(field)} = true;")
        }
        ln(1, s"return *this;")
        ln(s"}")
        endl()
      }

      // By-value-setters with 'const T&' in
      for (field <- coll.filter(CppGenerator.canBeNull)) {
        ln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"const ${getTypeName(field.typ(), false)} & ${field.name()}")} {")
        if (toSuper) {
          ln(1, s"$superString::${set(field, s"${field.name()}")};")
          ln(1, "return *this;")
        } else {
          ln(1, s"return ${set(field, s"${field.name()}._deepCopy(), true")};")
        }
        ln(s"}")
        endl()
      }

      for (field <- coll.filter(CppGenerator.canBeNull)) {
        ln(s"${t.shortName()}& ${t.shortName()}::${set(field, s"${getTypeName(field.typ(), false)} * ${field.name()}, const bool managePtr")} {")
        if (toSuper) {
          ln(1, s"$superString::${set(field, s"${field.name()}")};")
        } else {
          ln(1, s"m_${field.name()}.set(${field.name()}, managePtr);")
        }
        ln(1, s"return *this;")
        ln(s"}")
        endl()
      }

    }

  }

}
