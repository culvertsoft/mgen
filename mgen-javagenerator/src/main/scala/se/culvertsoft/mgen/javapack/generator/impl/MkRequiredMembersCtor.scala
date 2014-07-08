package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import Alias.isSetName
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.fieldTypeName
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkRequiredMembersCtor {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val reqAndOptFields = t.fieldsInclSuper().toBuffer
    val reqFields = t.fieldsInclSuper().filter(_.isRequired())

    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txtBuffer.tabs(1).text(s"public ${t.name()}(")
      for (i <- 0 until reqFields.size()) {
        val field = reqFields.get(i)
        val isLastField = i + 1 == reqFields.size()
        txtBuffer.tabs(if (i > 0) 4 else 0).text(s"final ${fieldTypeName(field)} ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(") {")

      val fieldsToSuper = reqFields -- t.fields
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

      val ownFields = reqFields -- fieldsToSuper
      for (field <- ownFields)
        txtBuffer.tabs(2).textln(s"m_${field.name()} = ${field.name()};")
      for (field <- ownFields) {
        if (!JavaGenerator.canBeNull(field))
          txtBuffer.tabs(2).textln(s"${isSetName(field)} = true;")
      }
      for (field <- (t.fields() -- ownFields)) {
        if (!JavaGenerator.canBeNull(field))
          txtBuffer.tabs(2).textln(s"${isSetName(field)} = false;")
      }
      txtBuffer.tabs(1).textln("}").endl()
    }
  }
}