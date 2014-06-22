package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._

object MkMetadataFieldMakers {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.getAllFieldsInclSuper())
      txtBuffer.tabs(1).textln(s"static std::vector<std::string> ${fieldMetaString(field, false)}_flags_make();")
    txtBuffer.tabs(1).textln("static std::vector<long long> _type_ids_make();");
    txtBuffer.tabs(1).textln("static std::vector<short> _type_ids_16bit_make();");
    txtBuffer.tabs(1).textln("static std::vector<std::string> _type_ids_16bit_base64_make();");
    txtBuffer.tabs(1).textln("static std::vector<std::string> _type_names_make();");
    txtBuffer.tabs(1).textln("static std::vector<mgen::Field> _field_metadatas_make();");
    txtBuffer.endl()

  }

}