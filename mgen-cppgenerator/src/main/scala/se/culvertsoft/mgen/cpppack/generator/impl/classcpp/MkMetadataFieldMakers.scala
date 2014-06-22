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

object MkMetadataFieldMakers {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.getAllFieldsInclSuper()) {
      txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::${fieldMetaString(field, false)}_flags_make() {")
      txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
      for (tag <- field.flags())
        txtBuffer.tabs(1).textln(s"out.push_back(${quote(tag)});")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

    txtBuffer.tabs(0).textln(s"std::vector<long long> ${t.shortName()}::_type_ids_make() {");
    txtBuffer.tabs(1).textln(s"std::vector<long long> out;")
    for (t <- t.superTypeHierarchy())
      txtBuffer.tabs(1).textln(s"out.push_back(${t.typeId()}LL);")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()
    
    txtBuffer.tabs(0).textln(s"std::vector<short> ${t.shortName()}::_type_ids_16bit_make() {");
    txtBuffer.tabs(1).textln(s"std::vector<short> out;")
    for (t <- t.superTypeHierarchy())
      txtBuffer.tabs(1).textln(s"out.push_back(${t.typeId16Bit});")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::_type_names_make() {");
    txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
    for (t <- t.superTypeHierarchy())
      txtBuffer.tabs(1).textln(s"out.push_back(${quote(t.fullName())});")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"std::vector<std::string> ${t.shortName()}::_type_ids_16bit_base64_make() {");
    txtBuffer.tabs(1).textln(s"std::vector<std::string> out;")
    for (t <- t.superTypeHierarchy())
      txtBuffer.tabs(1).textln(s"out.push_back(${quote(t.typeId16BitBase64())});")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"std::vector<mgen::Field> ${t.shortName()}::_field_metadatas_make() {");
    txtBuffer.tabs(1).textln(s"std::vector<mgen::Field> out;")
    for (field <- t.getAllFieldsInclSuper())
      txtBuffer.tabs(1).textln(s"out.push_back(${fieldMetaString(field)});")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}