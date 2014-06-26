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
import se.culvertsoft.mgen.cpppack.generator.CppGenerator

object MkIsFieldSet {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::_isFieldSet(const mgen::Field& field, const mgen::FieldSetDepth depth) const {")
    txtBuffer.tabs(1).textln(s"switch(field.id()) {")
    for (field <- t.getAllFieldsInclSuper()) {
      txtBuffer.tabs(2).textln(s"case (${fieldIdString(field)}):")
      txtBuffer.tabs(3).textln(s"return ${isFieldSet(field, "depth")};")
    }
    txtBuffer.tabs(2).textln(s"default:")
    txtBuffer.tabs(3).textln(s"return false;")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    for (field <- t.fields()) {
      txtBuffer.tabs(0).textln(s"bool ${t.shortName()}::${isFieldSet(field, "const mgen::FieldSetDepth depth")} const {")
      if (field.typ().containsMgenCreatedType()) {
        
        val shallowCall = if (CppGenerator.canBeNull(field)) s"m_${field.name()}.get()" else isSetName(field)
        
        txtBuffer.tabs(1).textln(s"if (depth == mgen::SHALLOW) {")
        txtBuffer.tabs(2).textln(s"return $shallowCall;")
        txtBuffer.tabs(1).textln(s"} else {")
        txtBuffer.tabs(2).textln(s"return $shallowCall && mgen::validation::validateFieldDeep(${get(field)});")
        txtBuffer.tabs(1).textln(s"}")
      } else {
        txtBuffer.tabs(1).textln(s"return ${isSetName(field)};")
      }
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

  }

}