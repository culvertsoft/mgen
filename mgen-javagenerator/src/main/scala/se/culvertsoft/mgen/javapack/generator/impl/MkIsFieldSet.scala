package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import Alias.isSetName
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldIfcClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.validatorClsString

object MkIsFieldSet {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    for (field <- t.fields()) {
      ln(1, s"public boolean ${isFieldSet(field, s"final ${fieldSetDepthClsString} fieldSetDepth")} {")
      if (field.typ.containsUserDefinedType) {
        ln(2, s"if (fieldSetDepth == ${fieldSetDepthClsString}.SHALLOW) {")
        ln(3, s"return ${isSetName(field)};")
        ln(2, s"} else {")
        ln(3, s"return ${isSetName(field)} && ${validatorClsString}.validateFieldDeep(${get(field)}, ${fieldMetadata(field)}.typ());")
        ln(2, s"}")
      } else {
        ln(2, s"return ${isSetName(field)};")
      }
      ln(1, s"}").endl()
    }

    ln(1, s"public boolean _isFieldSet(final $fieldIfcClsString field, final ${fieldSetDepthClsString} depth) {")
    ln(2, s"switch(field.id()) {")
    for (field <- t.fieldsInclSuper()) {
      ln(3, s"case (${fieldId(field)}):")
      ln(4, s"return ${isFieldSet(field, "depth")};")
    }
    ln(3, s"default:")
    ln(4, s"return false;")
    ln(2, s"}")
    ln(1, s"}").endl()
  }
}