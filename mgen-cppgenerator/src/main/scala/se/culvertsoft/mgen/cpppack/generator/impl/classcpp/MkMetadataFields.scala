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

object MkMetadataFields {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.getAllFieldsInclSuper()

    // Own type data
    txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_type_name() {")
    txtBuffer.tabs(1).textln(s"static const std::string out = ${quote(t.fullName())};")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<long long>& ${t.shortName()}::_type_ids() {")
    txtBuffer.tabs(1).textln(s"static const std::vector<long long> out = _type_ids_make();")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()
    
    txtBuffer.tabs(0).textln(s"const std::vector<short>& ${t.shortName()}::_type_ids_16bit() {")
    txtBuffer.tabs(1).textln(s"static const std::vector<short> out = _type_ids_16bit_make();")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_type_names() {")
    txtBuffer.tabs(1).textln(s"static const std::vector<std::string> out = _type_names_make();")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_type_ids_16bit_base64() {")
    txtBuffer.tabs(1).textln(s"static const std::vector<std::string> out = _type_ids_16bit_base64_make();")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_type_id_16bit_base64() {")
    txtBuffer.tabs(1).textln(s"static const std::string out = ${quote(t.typeId16BitBase64())};")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    // Field type data
    txtBuffer.tabs(0).textln(s"const std::vector<mgen::Field>& ${t.shortName()}::_field_metadatas() {")
    txtBuffer.tabs(1).textln(s"static const std::vector<mgen::Field> out = _field_metadatas_make();")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    // Fields metadata implementation
    for (field <- t.fields()) {
      val enumString = field.typ().typeEnum().toString()

      txtBuffer.tabs(0).textln(s"const mgen::Field& ${t.shortName()}::${fieldMetaString(field)} {")
      txtBuffer.tabs(1).textln(s"static const std::vector<std::string>& flags = ${fieldMetaString(field, false)}_flags_make();")
      txtBuffer.tabs(1).textln(
        s"static const mgen::Field out(${field.id()}, ${quote(field.name())}, mgen::Type(mgen::Type::ENUM_$enumString, mgen::Type::TAG_$enumString, flags), flags);")
      txtBuffer.tabs(1).textln(s"return out;")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

  }

}