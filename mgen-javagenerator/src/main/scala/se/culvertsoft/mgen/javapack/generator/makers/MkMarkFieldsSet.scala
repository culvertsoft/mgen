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

object MkMarkFieldsSet {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fields()
    val allFields = t.getAllFieldsInclSuper()

    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"public ${t.shortName()} ${setFieldSet(field, s"final boolean state, final ${fieldSetDepthClsString} depth")} {")
      txtBuffer.tabs(2).textln(s"${isSetName(field)} = state;")

      if (field.typ().containsMgenCreatedType()) {
        txtBuffer.tabs(2).textln(s"if (depth == ${fieldSetDepthClsString}.DEEP)")
        txtBuffer.tabs(3).textln(s"${setFieldSetClsString}.setFieldSetDeep(${get(field)}, ${fieldMetadata(field)}.typ());")
      }

      txtBuffer.tabs(2).textln(s"if (!state)")
      txtBuffer.tabs(3).textln(s"m_${field.name()} = ${defaultConstructNull(field.typ())};")

      txtBuffer.tabs(2).textln(s"return this;")
      txtBuffer.tabs(1).textln(s"}")
      txtBuffer.endl()
    }

    txtBuffer.tabs(1).textln(s"public ${t.shortName()} _setAllFieldsSet(final boolean state, final ${fieldSetDepthClsString} depth) { ")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"${setFieldSet(field, "state, depth")};")
    txtBuffer.tabs(2).textln(s"return this;")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()

  }
}