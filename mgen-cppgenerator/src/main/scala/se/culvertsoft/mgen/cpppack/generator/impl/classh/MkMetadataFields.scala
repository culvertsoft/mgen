package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkMetadataFields {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module
    
    // Own type metadata    
    ln(1, s"static const long long _type_id = ${t.typeId()}LL;")
    ln(1, "static const std::vector<long long>& _type_ids();").endl()

    ln(1, s"static const short _type_id_16bit = ${t.typeId16Bit()};")
    ln(1, "static const std::vector<short>& _type_ids_16bit();").endl()

    ln(1, "static const std::string& _type_id_16bit_base64();")
    ln(1, "static const std::vector<std::string>& _type_ids_16bit_base64();").endl()

    ln(1, "static const std::string& _type_ids_16bit_base64_string();").endl()
    
    ln(1, "static const std::string& _type_name();")
    ln(1, "static const std::vector<std::string>& _type_names();").endl()

    // Field metadata
    for (field <- t.fields())
      ln(1, s"static const mgen::Field& ${fieldMetaString(field)};")
    endl()

    for ((field, i) <- t.fields().zipWithIndex)
      ln(1, s"static const short ${fieldIdString(field)} = ${field.id()};")
    endl()

    ln(1, s"static const std::vector<mgen::Field>& _field_metadatas();").endl()

  }

}