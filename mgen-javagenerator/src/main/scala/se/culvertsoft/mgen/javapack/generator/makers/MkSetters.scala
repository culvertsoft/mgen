package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isSetName
import Alias.set
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkSetters {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val thisFields = t.fields()
    val superFields = t.getAllFieldsInclSuper() -- thisFields

    for (field <- thisFields) {
      txtBuffer.tabs(1).textln(s"public ${t.name()} ${set(field, s"final ${getTypeName(field.typ())} ${field.name()}")} {")
      txtBuffer.tabs(2).textln(s"m_${field.name()} = ${field.name()};")
      txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    for (field <- superFields) {
      txtBuffer.tabs(1).textln(s"public ${t.name()} ${set(field, s"final ${getTypeName(field.typ())} ${field.name()}")} {")
      txtBuffer.tabs(2).textln(s"super.${set(field, field.name())};")
      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}").endl()
    }

  }
}