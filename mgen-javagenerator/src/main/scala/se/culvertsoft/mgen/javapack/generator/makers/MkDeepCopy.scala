package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import Alias.set
import Alias.setFieldSet
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.deepCopyerClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkDeepCopy {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public ${t.shortName()} deepCopy() {")
    txtBuffer.tabs(2).textln(s"final ${t.shortName()} out = new ${t.shortName()}();")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"out.${set(field, s"${deepCopyerClsString}.deepCopy(${get(field)}, ${fieldMetadata(field)}.typ())")};")
    for (field <- allFields) {
      val shallow = s"${fieldSetDepthClsString}.SHALLOW"
      val isFieldSetString = isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")
      txtBuffer.tabs(2).textln(s"out.${setFieldSet(field, s"$isFieldSetString, $shallow")};")
    }
    txtBuffer.tabs(2).textln("return out;")
    txtBuffer.tabs(1).textln("}").endl()

  }
}