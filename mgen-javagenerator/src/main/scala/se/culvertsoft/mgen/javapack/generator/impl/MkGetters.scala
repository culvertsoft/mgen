package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.get
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.declared

object MkGetters {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    for (field <- t.fields()) {
      ln(1, s"public ${declared(field)} ${get(field)} {")
      ln(2, s"return m_${field.name()};")
      ln(1, s"}").endl()
    }

    for (field <- t.fields()) {
      ln(1, s"public boolean has${upFirst(field.name())}() {")
      ln(2, s"return ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")};")
      ln(1, s"}").endl()
    }

    for (field <- t.fieldsInclSuper()) {
      ln(1, s"public ${declared(t, false)} unset${upFirst(field.name())}() {")
      ln(2, s"_set${upFirst(field.name())}Set(false, ${fieldSetDepthClsString}.SHALLOW);")
      ln(2, s"return this;")
      ln(1, s"}").endl()
    }
  }
}