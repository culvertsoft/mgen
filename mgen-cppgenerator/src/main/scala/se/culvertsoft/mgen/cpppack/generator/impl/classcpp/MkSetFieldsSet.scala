package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._

object MkSetFieldsSet {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val fields = t.fields()
    val allFields = t.getAllFieldsInclSuper()

    for (field <- fields) {
      txtBuffer.tabs(0).textln(s"${t.shortName()}& ${t.shortName()}::${setFieldSet(field, "const bool state, const mgen::FieldSetDepth depth")} {")
      if (!field.typ().containsMgenCreatedType()) {
        txtBuffer.tabs(1).textln(s"${isSetName(field)} = state;")
      } else {
        txtBuffer.tabs(1).textln(s"${isSetName(field)} = state;")
        txtBuffer.tabs(1).textln(s"if (depth == mgen::DEEP)")
        txtBuffer.tabs(2).textln(s"mgen::validation::setFieldSetDeep(m_${field.name()});")
      }
      txtBuffer.tabs(1).textln(s"return *this;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

    txtBuffer.tabs(0).textln(s"${t.shortName()}& ${t.shortName()}::_setAllFieldsSet(const bool state, const mgen::FieldSetDepth depth) { ")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"${setFieldSet(field, "state, depth")};")
    txtBuffer.tabs(1).textln(s"return *this;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}