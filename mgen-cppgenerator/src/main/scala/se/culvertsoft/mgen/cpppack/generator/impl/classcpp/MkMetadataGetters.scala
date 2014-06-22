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

object MkMetadataGetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(0).textln(s"const long long ${t.shortName()}::_typeId() const {")
    txtBuffer.tabs(1).textln(s"return _type_id;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_typeName() const {")
    txtBuffer.tabs(1).textln(s"return _type_name();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const short ${t.shortName()}::_typeId16Bit() const {")
    txtBuffer.tabs(1).textln(s"return _type_id_16bit;")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<long long>& ${t.shortName()}::_typeIds() const {")
    txtBuffer.tabs(1).textln(s"return _type_ids();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()
    
    txtBuffer.tabs(0).textln(s"const std::vector<short>& ${t.shortName()}::_typeIds16Bit() const {")
    txtBuffer.tabs(1).textln(s"return _type_ids_16bit();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::string& ${t.shortName()}::_typeId16BitBase64() const {")
    txtBuffer.tabs(1).textln(s"return _type_id_16bit_base64();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_typeNames() const {")
    txtBuffer.tabs(1).textln(s"return _type_names();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<std::string>& ${t.shortName()}::_typeIds16BitBase64() const {")
    txtBuffer.tabs(1).textln(s"return _type_ids_16bit_base64();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(0).textln(s"const std::vector<mgen::Field>& ${t.shortName()}::_fieldMetadatas() const {")
    txtBuffer.tabs(1).textln(s"return _field_metadatas();")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()


  }

}