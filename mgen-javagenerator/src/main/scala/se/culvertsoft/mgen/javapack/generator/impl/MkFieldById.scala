package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import Alias.fieldId
import Alias.fieldMetadata
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkFieldById {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()

    ln(1, "@Override")
    ln(1, s"public $fieldIfcClsString _fieldById(final short fieldId) {")
    ln(2, s"switch(fieldId) {")
    for (field <- allFields) {
      ln(3, s"case (${fieldId(field)}):")
      ln(4, s"return ${fieldMetadata(field)};")
    }
    ln(3, s"default:")
    ln(4, s"return null;")
    ln(2, s"}")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, s"public $fieldIfcClsString _fieldByName(final String fieldName) {")
    ln(2, s"switch(fieldName) {")
    for (field <- allFields) {
      ln(3, s"case (${quote(field.name)}):")
      ln(4, s"return ${fieldMetadata(field)};")
    }
    ln(3, s"default:")
    ln(4, s"return null;")
    ln(2, s"}")
    ln(1, "}").endl()

  }

}