package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import Alias.isSetName
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.fieldTypeName

object MkRequiredMembersCtor {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val reqAndOptFields = t.fieldsInclSuper().toBuffer
    val reqFields = t.fieldsInclSuper().filter(_.isRequired())

    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txt(1, s"public ${t.shortName}(")
      for (i <- 0 until reqFields.size()) {
        val field = reqFields.get(i)
        val isLastField = i + 1 == reqFields.size()
        txt(if (i > 0) 4 else 0, s"final ${fieldTypeName(field)} ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      ln(") {")

      val fieldsToSuper = reqFields -- t.fields
      if (fieldsToSuper.nonEmpty) {
        txt(2, "super(")
        for (i <- 0 until fieldsToSuper.size()) {
          val field = fieldsToSuper.get(i)
          val isLastField = i + 1 == fieldsToSuper.size()
          txt(field.name())
          if (!isLastField) {
            txt(", ")
          }
        }
        ln(");")
      }

      for (field <- t.fields) {
        if (field.isRequired)
          ln(2, s"m_${field.name()} = ${field.name()};")
        else
          ln(2, s"m_${field.name()} = ${MkDefaultValue(field, false)};")
      }

      for (f <- t.fields) {
        if (!JavaGenerator.canBeNull(f)) {
          ln(2, s"${isSetName(f)} = ${f.isRequired || f.hasDefaultValue};")
        }
      }
      ln(1, "}").endl()
    }
  }
}