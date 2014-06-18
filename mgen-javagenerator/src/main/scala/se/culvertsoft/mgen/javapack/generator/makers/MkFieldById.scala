package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import Alias.fieldMetadata
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldClsString

object MkFieldById {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public $fieldClsString _fieldById(final short fieldId) {")
    txtBuffer.tabs(2).textln(s"switch(fieldId) {")
    for (field <- allFields) {
      txtBuffer.tabs(3).textln(s"case (${fieldId(field)}):")
      txtBuffer.tabs(4).textln(s"return ${fieldMetadata(field)};")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"return null;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln("}").endl()
  }
}