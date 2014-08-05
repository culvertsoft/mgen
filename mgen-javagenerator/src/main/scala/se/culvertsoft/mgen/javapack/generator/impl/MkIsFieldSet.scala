package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import Alias.isSetName
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldIfcClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.validatorClsString

object MkIsFieldSet {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public boolean ${isFieldSet(field, s"final ${fieldSetDepthClsString} fieldSetDepth")} {")
      if (field.typ.containsUserDefinedType) {
        txtBuffer.tabs(2).textln(s"if (fieldSetDepth == ${fieldSetDepthClsString}.SHALLOW) {")
        txtBuffer.tabs(3).textln(s"return ${isSetName(field)};")
        txtBuffer.tabs(2).textln(s"} else {")
        txtBuffer.tabs(3).textln(s"return ${isSetName(field)} && ${validatorClsString}.validateFieldDeep(${get(field)}, ${fieldMetadata(field)}.typ());")
        txtBuffer.tabs(2).textln(s"}")
      } else {
        txtBuffer.tabs(2).textln(s"return ${isSetName(field)};")
      }
      txtBuffer.tabs(1).textln(s"}").endl()
    }

    txtBuffer.tabs(1).textln(s"public boolean _isFieldSet(final $fieldIfcClsString field, final ${fieldSetDepthClsString} depth) {")
    txtBuffer.tabs(2).textln(s"switch(field.id()) {")
    for (field <- t.fieldsInclSuper()) {
      txtBuffer.tabs(3).textln(s"case (${fieldId(field)}):")
      txtBuffer.tabs(4).textln(s"return ${isFieldSet(field, "depth")};")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"return false;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln(s"}").endl()
  }
}