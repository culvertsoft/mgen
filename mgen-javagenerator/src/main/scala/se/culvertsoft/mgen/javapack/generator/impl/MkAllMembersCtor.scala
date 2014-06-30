package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import Alias.isSetName
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.fieldTypeName
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkAllMembersCtor {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()
    if (allFields.nonEmpty) {
      txtBuffer.tabs(1).text(s"public ${t.name()}(")
      for (i <- 0 until allFields.size()) {
        val field = allFields.get(i)
        val isLastField = i + 1 == allFields.size()
        txtBuffer.tabs(if (i > 0) 4 else 0).text(s"final ${fieldTypeName(field)} ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(") {")

      val fieldsToSuper = allFields -- t.fields
      if (fieldsToSuper.nonEmpty) {
        txtBuffer.tabs(2).text("super(")
        for (i <- 0 until fieldsToSuper.size()) {
          val field = fieldsToSuper.get(i)
          val isLastField = i + 1 == fieldsToSuper.size()
          txtBuffer.text(field.name())
          if (!isLastField) {
            txtBuffer.text(", ")
          }
        }
        txtBuffer.textln(");")
      }

      for (field <- t.fields())
        txtBuffer.tabs(2).textln(s"m_${field.name()} = ${field.name()};")
      for (field <- t.fields()) {
        if (!JavaGenerator.canBeNull(field))
          txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
      }

      txtBuffer.tabs(1).textln("}").endl()
    }
  }
}