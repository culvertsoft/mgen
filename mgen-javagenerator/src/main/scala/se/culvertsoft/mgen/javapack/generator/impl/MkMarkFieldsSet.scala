package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import Alias.fieldMetadata
import Alias.get
import Alias.isSetName
import Alias.setFieldSet
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.setFieldSetClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstruction._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkMarkFieldsSet {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fields()
    val allFields = t.fieldsInclSuper()

    for (field <- fields) {
      ln(1, s"public ${t.shortName()} ${setFieldSet(field, s"final boolean state, final ${fieldSetDepthClsString} depth")} {")

      if (JavaGenerator.canBeNull(field)) {
        ln(2, "if (state)")
        ln(3, s"m_${field.name} = m_${field.name} != null ? m_${field.name} : ${MkDefaultValue(field)};")
        ln(2, "else")
        ln(3, s"m_${field.name} = null;")
      } else {
        ln(2, "if (!state)")
        ln(3, s"m_${field.name} = ${MkDefaultValue(field)};")
        ln(2, s"${isSetName(field)} = state;")
      }

      if (field.typ().containsCustomType()) {
        ln(2, s"if (depth == ${fieldSetDepthClsString}.DEEP)")
        ln(3, s"${setFieldSetClsString}.setFieldSetDeep(${get(field)}, ${fieldMetadata(field)}.typ());")
      }
      
      ln(2, s"return this;")
      ln(1, s"}")
      endl()
    }

    ln(1, s"public ${t.shortName()} _setAllFieldsSet(final boolean state, final ${fieldSetDepthClsString} depth) { ")
    for (field <- allFields)
      ln(2, s"${setFieldSet(field, "state, depth")};")
    ln(2, s"return this;")
    ln(1, s"}")
    endl()

  }
}