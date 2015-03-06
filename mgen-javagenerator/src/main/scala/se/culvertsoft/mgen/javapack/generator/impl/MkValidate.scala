package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkValidate {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    ln(1, s"public boolean _validate(final ${fieldSetDepthClsString} fieldSetDepth) { ")
    ln(2, s"if (fieldSetDepth == ${fieldSetDepthClsString}.SHALLOW) {")
    txt(3, s"return true")
    for (field <- t.fieldsInclSuper().filter(_.isRequired()))
      ln().tabs(4).text(s"&& ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")}")
    ln(s";")
    ln(2, s"} else {")
    txt(3, s"return true")
    for (field <- t.fieldsInclSuper()) {
      if (field.isRequired())
        ln().tabs(4).text(s"&& ${isFieldSet(field, s"${fieldSetDepthClsString}.DEEP")}")
      else if (field.typ.containsUserDefinedType)
        ln().tabs(4).text(s"&& (!${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")} || ${isFieldSet(field, s"${fieldSetDepthClsString}.DEEP")})")
    }
    ln(s";")
    ln(2, s"}")
    ln(1, s"}")
    ln()

  }
}