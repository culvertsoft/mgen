package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldMetaString

object MkMetadataFieldMakers {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module
    
    for (field <- t.fieldsInclSuper())
      ln(1, s"static std::vector<std::string> ${fieldMetaString(field, false)}_flags_make();")
    ln(1, "static std::vector<long long> _type_ids_make();");
    ln(1, "static std::vector<short> _type_ids_16bit_make();");
    ln(1, "static std::vector<std::string> _type_ids_16bit_base64_make();");
    ln(1, "static std::vector<std::string> _type_names_make();");
    ln(1, "static std::vector<mgen::Field> _field_metadatas_make();");
    ln(1, "static std::map<std::string, const mgen::Field*> _field_names2metadata_make();");
    endl()

  }

}