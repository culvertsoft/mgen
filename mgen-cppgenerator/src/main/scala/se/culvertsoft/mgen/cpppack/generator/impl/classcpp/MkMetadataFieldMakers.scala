package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldMetaString

object MkMetadataFieldMakers {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module
    
    for (field <- t.getAllFieldsInclSuper()) {
      ln(s"std::vector<std::string> ${t.shortName()}::${fieldMetaString(field, false)}_flags_make() {")
      ln(1, s"std::vector<std::string> out;")
      for (tag <- field.flags())
        ln(1, s"out.push_back(${quote(tag)});")
      ln(1, s"return out;")
      ln(s"}")
      endl()
    }

    ln(s"std::vector<long long> ${t.shortName()}::_type_ids_make() {");
    ln(1, s"std::vector<long long> out;")
    for (t <- t.superTypeHierarchy())
      ln(1, s"out.push_back(${t.typeId()}LL);")
    ln(1, s"return out;")
    ln(s"}")
    endl()
    
    ln(s"std::vector<short> ${t.shortName()}::_type_ids_16bit_make() {");
    ln(1, s"std::vector<short> out;")
    for (t <- t.superTypeHierarchy())
      ln(1, s"out.push_back(${t.typeId16Bit});")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"std::vector<std::string> ${t.shortName()}::_type_names_make() {");
    ln(1, s"std::vector<std::string> out;")
    for (t <- t.superTypeHierarchy())
      ln(1, s"out.push_back(${quote(t.fullName())});")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"std::vector<std::string> ${t.shortName()}::_type_ids_16bit_base64_make() {");
    ln(1, s"std::vector<std::string> out;")
    for (t <- t.superTypeHierarchy())
      ln(1, s"out.push_back(${quote(t.typeId16BitBase64())});")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"std::vector<mgen::Field> ${t.shortName()}::_field_metadatas_make() {");
    ln(1, s"std::vector<mgen::Field> out;")
    for (field <- t.getAllFieldsInclSuper())
      ln(1, s"out.push_back(${fieldMetaString(field)});")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"std::map<std::string, const mgen::Field*> ${t.shortName()}::_field_names2metadata_make() {");
    ln(1, s"std::map<std::string, const mgen::Field*> out;")
    for (field <- t.getAllFieldsInclSuper())
      ln(1, s"out[${quote(field.name)}] = &${fieldMetaString(field)};")
    ln(1, s"return out;")
    ln(s"}")
    endl()
    
  }

}