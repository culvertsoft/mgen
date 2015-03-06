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
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.declared

object MkAllMembersCtor {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()
    if (allFields.nonEmpty) {
      txt(1, s"public ${t.shortName}(")
      for (i <- 0 until allFields.size()) {
        val field = allFields.get(i)
        val isLastField = i + 1 == allFields.size()
        txt(if (i > 0) 4 else 0, s"final ${declared(field)} ${field.name()}")
        if (!isLastField) {
          ln(",")
        }
      }
      ln(") {")

      val fieldsToSuper = allFields -- t.fields
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

      for (field <- t.fields())
        ln(2, s"m_${field.name()} = ${field.name()};")
      for (field <- t.fields()) {
        if (!JavaGenerator.canBeNull(field))
          ln(2, s"${isSetName(field)} = true;")
      }

      ln(1, "}").endl()
    }
  }
}