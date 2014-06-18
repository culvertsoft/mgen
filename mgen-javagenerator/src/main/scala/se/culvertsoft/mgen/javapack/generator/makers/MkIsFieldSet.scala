package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.javapack.generator.JavaConstruction._
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames._
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.javapack.generator.JavaToString
import se.culvertsoft.mgen.javapack.generator.JavaReadCalls._
import se.culvertsoft.mgen.api.model.TypeEnum

object MkIsFieldSet {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    for (field <- t.fields()) {
      txtBuffer.tabs(1).textln(s"public boolean ${isFieldSet(field, s"final ${fieldSetDepthClsString} fieldSetDepth")} {")
      if (field.typ().containsMgenCreatedType()) {
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

    txtBuffer.tabs(1).textln(s"public boolean _isFieldSet(final $fieldClsString field, final ${fieldSetDepthClsString} depth) {")
    txtBuffer.tabs(2).textln(s"switch(field.id()) {")
    for (field <- t.getAllFieldsInclSuper()) {
      txtBuffer.tabs(3).textln(s"case (${fieldId(field)}):")
      txtBuffer.tabs(4).textln(s"return ${isFieldSet(field, "depth")};")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"return false;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln(s"}").endl()
  }
}