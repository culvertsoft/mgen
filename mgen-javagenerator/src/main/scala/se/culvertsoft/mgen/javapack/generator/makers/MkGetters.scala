package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer

import Alias.get
import Alias.isFieldSet
import Alias.isSetName
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkGetters {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public ${getTypeName(field.typ())} ${get(field)} {")
      txtBuffer.tabs(2).textln(s"return m_${field.name()};")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    for (field <- t.fields()) {
      if (!field.typ().isSimple()) {
        txtBuffer.tabs(1).textln(s"public ${getTypeName(field.typ())} ${get(field, "Mutable")} {")
        txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
        txtBuffer.tabs(2).textln(s"return m_${field.name()};")
        txtBuffer.tabs(1).textln(s"}").endl()
      }
    }

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public boolean has${upFirst(field.name())}() {")
      txtBuffer.tabs(2).textln(s"return ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")};")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    for (field <- t.getAllFieldsInclSuper()) {
      txtBuffer.tabs(1).textln(s"public ${t.shortName()} unset${upFirst(field.name())}() {")
      txtBuffer.tabs(2).textln(s"_set${upFirst(field.name())}Set(false, ${fieldSetDepthClsString}.SHALLOW);")
      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}").endl()
    }
  }
}