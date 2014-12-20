package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldMetaString

object MkMetadataFields {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    val allFields = t.fieldsInclSuper()

    val pfx = s"_${t.shortName}"

    // Own type data
    ln(s"const std::string& ${t.shortName()}::_type_name() {")
    ln(1, s"static const std::string out(${quote(t.fullName)});")
    ln(1, "return out;")
    ln("}")
    endl()

    {
      val ids = t.typeHierarchy.map(_.typeId.toString + "LL")
      ln(s"const std::vector<long long>& ${t.shortName()}::_type_ids() {")
      ln(1, s"static const std::vector<long long> out = mgen::make_vector<long long>() << ${ids.mkString(" << ")};")
      ln(1, s"return out;")
      ln("}")
      endl()
    }

    {
      val ids16bit = t.typeHierarchy.map(_.typeId16Bit.toString)
      ln(s"const std::vector<short>& ${t.shortName()}::_type_ids_16bit() {")
      ln(1, s"static const std::vector<short> out = mgen::make_vector<short>() << ${ids16bit.mkString(" << ")};")
      ln(1, "return out;")
      ln("}")
      endl()
    }

    {
      val names = t.typeHierarchy.map(x => quote(x.fullName))
      ln(s"const std::vector<std::string>& ${t.shortName()}::_type_names() {")
      ln(1, s"static const std::vector<std::string> out = mgen::make_vector<std::string>() << ${names.mkString(" << ")};")
      ln(1, "return out;")
      ln("}")
      endl()
    }

    {
      val ids = t.typeHierarchy.map(x => quote(x.typeId16BitBase64))
      ln(s"const std::vector<std::string>& ${t.shortName()}::_type_ids_16bit_base64() {")
      ln(1, s"static const std::vector<std::string> out = mgen::make_vector<std::string>() << ${ids.mkString(" << ")};")
      ln(1, "return out;")
      ln("}")
      endl()
    }

    val base64ids = t.typeHierarchy().map(_.typeId16BitBase64())
    val base64String = quote(base64ids.mkString(""))

    ln(s"const std::string& ${t.shortName()}::_type_ids_16bit_base64_string() {")
    ln(1, s"static const std::string out($base64String);")
    ln(1, "return out;")
    ln("}")
    endl()

    ln(s"const std::string& ${t.shortName()}::_type_id_16bit_base64() {")
    ln(1, s"static const std::string out(${quote(t.typeId16BitBase64)});")
    ln(1, "return out;")
    ln("}")
    endl()

    // Field type data
    ln(s"const std::vector<mgen::Field>& ${t.shortName()}::_field_metadatas() {")
    val metadatas = t.fieldsInclSuper.map(fieldMetaString(_))
    val metadatasString = if (metadatas.isEmpty) ";" else s" = mgen::make_vector<mgen::Field>() << ${metadatas.mkString(" << ")};"

    ln(1, s"static const std::vector<mgen::Field> out${metadatasString}")
    ln(1, "return out;")
    ln("}")
    endl()

    // Fields metadata implementation
    for (field <- t.fields()) {

      val enumString = field.typ.typeEnum.toString
      val tagString = enumString match {
        case "ENUM" => "STRING"
        case _ => enumString
      }

      ln(s"const mgen::Field& ${t.shortName()}::${fieldMetaString(field)} {")

      val flagsString = if (field.flags.nonEmpty) s", mgen::make_vector<std::string>() << ${field.flags.map(quote).mkString(" << ")}" else ""

      ln(1, s"static const mgen::Field out(${field.id}, ${quote(field.name)}$flagsString);")
      ln(1, "return out;")
      ln("}")
      endl()
    }

  }

}