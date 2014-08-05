package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkValidate {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    txtBuffer.tabs(1).textln(s"public boolean _validate(final ${fieldSetDepthClsString} fieldSetDepth) { ")
    txtBuffer.tabs(2).textln(s"if (fieldSetDepth == ${fieldSetDepthClsString}.SHALLOW) {")
    txtBuffer.tabs(3).text(s"return true")
    for (field <- t.fieldsInclSuper().filter(_.isRequired()))
      txtBuffer.endl().tabs(4).text(s"&& ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")}")
    txtBuffer.textln(s";")
    txtBuffer.tabs(2).textln(s"} else {")
    txtBuffer.tabs(3).text(s"return true")
    for (field <- t.fieldsInclSuper()) {
      if (field.isRequired())
        txtBuffer.endl().tabs(4).text(s"&& ${isFieldSet(field, s"${fieldSetDepthClsString}.DEEP")}")
      else if (field.typ.containsUserDefinedType)
        txtBuffer.endl().tabs(4).text(s"&& (!${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")} || ${isFieldSet(field, s"${fieldSetDepthClsString}.DEEP")})")
    }
    txtBuffer.textln(s";")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()

  }
}