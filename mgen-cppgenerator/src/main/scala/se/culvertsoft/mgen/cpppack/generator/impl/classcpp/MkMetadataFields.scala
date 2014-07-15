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

    val allFields = t.fieldsInclSuper()

    val pfx = s"_${t.shortName}"

    // Own type data
    ln(s"const std::string& ${t.shortName()}::_type_name() {")
    ln(1, s"static const std::string out = ${quote(t.fullName())};")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"const std::vector<long long>& ${t.shortName()}::_type_ids() {")
    ln(1, s"static const std::vector<long long> out = ${pfx}_type_ids_make();")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"const std::vector<short>& ${t.shortName()}::_type_ids_16bit() {")
    ln(1, s"static const std::vector<short> out = ${pfx}_type_ids_16bit_make();")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"const std::vector<std::string>& ${t.shortName()}::_type_names() {")
    ln(1, s"static const std::vector<std::string> out = ${pfx}_type_names_make();")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"const std::vector<std::string>& ${t.shortName()}::_type_ids_16bit_base64() {")
    ln(1, s"static const std::vector<std::string> out = ${pfx}_type_ids_16bit_base64_make();")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    val base64ids = t.superTypeHierarchy().map(_.typeId16BitBase64())
    val base64String = quote(base64ids.mkString(""))

    ln(s"const std::string& ${t.shortName()}::_type_ids_16bit_base64_string() {")
    ln(1, s"static const std::string out($base64String);")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    ln(s"const std::string& ${t.shortName()}::_type_id_16bit_base64() {")
    ln(1, s"static const std::string out = ${quote(t.typeId16BitBase64())};")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    // Field type data
    ln(s"const std::vector<mgen::Field>& ${t.shortName()}::_field_metadatas() {")
    ln(1, s"static const std::vector<mgen::Field> out = ${pfx}_field_metadatas_make();")
    ln(1, s"return out;")
    ln(s"}")
    endl()

    // Fields metadata implementation
    for (field <- t.fields()) {

      val enumString = field.typ().typeEnum().toString()
      val tagString = enumString match {
        case "ENUM" => "STRING"
        case _ => enumString
      }

      ln(s"const mgen::Field& ${t.shortName()}::${fieldMetaString(field)} {")
      val flagsString = s"${pfx}${fieldMetaString(field, false)}_flags_make()"
      ln(1,
        s"static const mgen::Field out(${field.id()}, ${quote(field.name())}, mgen::Type(mgen::Type::ENUM_$enumString, mgen::Type::TAG_$tagString), $flagsString);")
      ln(1, s"return out;")
      ln(s"}")
      endl()
    }

  }

}