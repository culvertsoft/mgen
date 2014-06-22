package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._

object MkAllMembersCtor {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.getAllFieldsInclSuper()
    if (allFields.nonEmpty) {
      txtBuffer.tabs(0).text(s"${t.name()}::${t.name()}(")
      for (i <- 0 until allFields.size()) {
        val field = allFields.get(i)
        val isLastField = i + 1 == allFields.size()
        txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(") : ")

      val fieldsToSuper = allFields -- t.fields
      if (fieldsToSuper.nonEmpty) {
        txtBuffer.tabs(2).text(s"${CppGenUtils.getSuperTypeString(t)}(")
        for (i <- 0 until fieldsToSuper.size()) {
          val field = fieldsToSuper.get(i)
          val isLastField = i + 1 == fieldsToSuper.size()
          txtBuffer.text(field.name())
          if (!isLastField) {
            txtBuffer.text(", ")
          }
        }
        txtBuffer.text(")")
      }

      if (t.fields().nonEmpty) {
        if (fieldsToSuper.nonEmpty) {
          txtBuffer.textln(",")
        }

        for ((field, i) <- t.fields().zipWithIndex) {
          txtBuffer.tabs(2).textln(s"m_${field.name()}(${field.name()}),")
        }

        for ((field, i) <- t.fields().zipWithIndex) {
          txtBuffer.tabs(2).text(s"${isSetName(field)}(true)")
          if (i + 1 < t.fields().size()) {
            txtBuffer.comma().endl()
          }
        }

      }
      txtBuffer.tabs(0).textln(" {")
      txtBuffer.tabs(0).textln("}").endl()
    }
  }

}