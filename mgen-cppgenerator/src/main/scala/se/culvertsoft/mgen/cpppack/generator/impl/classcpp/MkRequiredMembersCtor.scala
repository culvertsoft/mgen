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

object MkRequiredMembersCtor {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module
    val reqAndOptFields = t.getAllFieldsInclSuper().toBuffer
    val reqFields = t.getAllFieldsInclSuper().filter(_.isRequired())
    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txtBuffer.tabs(0).text(s"${t.name()}::${t.name()}(")
      for (i <- 0 until reqFields.size) {
        val field = reqFields(i)
        val isLastField = i + 1 == reqFields.size
        txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(") : ")

      val fieldsToSuper = reqFields -- t.fields
      if (fieldsToSuper.nonEmpty) {
        txtBuffer.tabs(2).text(s"${CppGenUtils.getSuperTypeString(t)}(")
        for (i <- 0 until fieldsToSuper.size) {
          val field = fieldsToSuper(i)
          val isLastField = i + 1 == fieldsToSuper.size
          txtBuffer.text(field.name())
          if (!isLastField) {
            txtBuffer.text(", ")
          }
        }
        txtBuffer.text(")")
      }

      if (fieldsToSuper.nonEmpty && t.fields().nonEmpty)
        txtBuffer.textln(",")

      for (field <- t.fields()) {
        if (field.isRequired())
          txtBuffer.tabs(2).textln(s"m_${field.name()}(${field.name()}),")
        else
          txtBuffer.tabs(2).textln(s"m_${field.name()}(${CppConstruction.defaultConstruct(field)}),")
      }

      for ((field, i) <- t.fields().zipWithIndex) {
        if (field.isRequired())
          txtBuffer.tabs(2).text(s"${isSetName(field)}(true)")
        else
          txtBuffer.tabs(2).text(s"${isSetName(field)}(false)")
        if (i + 1 < t.fields().size()) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln("{")
      txtBuffer.tabs(0).textln("}").endl()
    }

  }

}