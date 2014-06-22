package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._

object MkMetadataFields {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    // Own type metadata    
    txtBuffer.tabs(1).textln(s"static const long long _type_id = ${t.typeId()}LL;")
    txtBuffer.tabs(1).textln("static const std::vector<long long>& _type_ids();").endl()

    txtBuffer.tabs(1).textln(s"static const short _type_id_16bit = ${t.typeId16Bit()};")
    txtBuffer.tabs(1).textln("static const std::vector<short>& _type_ids_16bit();").endl()

    txtBuffer.tabs(1).textln("static const std::string& _type_id_16bit_base64();")
    txtBuffer.tabs(1).textln("static const std::vector<std::string>& _type_ids_16bit_base64();").endl()

    txtBuffer.tabs(1).textln("static const std::string& _type_name();")
    txtBuffer.tabs(1).textln("static const std::vector<std::string>& _type_names();").endl()

    // Field metadata
    for (field <- t.fields())
      txtBuffer.tabs(1).textln(s"static const mgen::Field& ${fieldMetaString(field)};")
    txtBuffer.endl()

    for ((field, i) <- t.fields().zipWithIndex)
      txtBuffer.tabs(1).textln(s"static const short ${fieldIdString(field)} = ${field.id()};")
    txtBuffer.endl()

    txtBuffer.tabs(1).textln(s"static const std::vector<mgen::Field>& _field_metadatas();").endl()

  }

}