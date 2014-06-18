package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.eqTesterClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkEquals {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public boolean equals(final Object other) {")
    txtBuffer.tabs(2).textln(s"if (other == null) return false;")
    txtBuffer.tabs(2).textln(s"if (other == this) return true;")
    txtBuffer.tabs(2).textln(s"if (${t.name()}.class != other.getClass()) return false;")

    if (allFields.nonEmpty)
      txtBuffer.tabs(2).textln(s"final ${t.name()} o = (${t.name()})other;")
    txtBuffer.tabs(2).text("return true")
    for (field <- allFields) {
      txtBuffer.endl().tabs(2).text(s"  && (${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")} == o.${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")})")
    }
    for (field <- allFields) {
      txtBuffer.endl().tabs(2).text(s"  && ${eqTesterClsString}.areEqual(${get(field)}, o.${get(field)}, ${fieldMetadata(field)}.typ())")
    }
    txtBuffer.textln(";")
    txtBuffer.tabs(1).textln("}").endl()
  }
}