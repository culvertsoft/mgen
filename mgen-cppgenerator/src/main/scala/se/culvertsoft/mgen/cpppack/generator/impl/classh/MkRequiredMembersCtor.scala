package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName

object MkRequiredMembersCtor {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val reqAndOptFields = t.getAllFieldsInclSuper().toBuffer
    val reqFields = t.getAllFieldsInclSuper().filter(_.isRequired())

    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txtBuffer.tabs(1).text(s"${t.name()}(")
      for (i <- 0 until reqFields.size) {
        val field = reqFields(i)
        val isLastField = i + 1 == reqFields.size
        txtBuffer.tabs(if (i > 0) 3 else 0).text(s"const ${getTypeName(field)}& ${field.name()}")
        if (!isLastField) {
          txtBuffer.comma().endl()
        }
      }
      txtBuffer.textln(");")
    }

  }

}