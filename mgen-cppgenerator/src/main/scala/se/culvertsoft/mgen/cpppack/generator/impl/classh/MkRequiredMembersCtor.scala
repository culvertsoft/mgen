package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName

object MkRequiredMembersCtor {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val module = t.module

    val reqAndOptFields = t.fieldsInclSuper().toBuffer
    val reqFields = t.fieldsInclSuper().filter(_.isRequired())

    if (reqFields.nonEmpty && reqAndOptFields != reqFields) {
      txtBuffer.tabs(1).text(s"${t.shortName}(")
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