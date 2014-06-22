package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkMetadataGetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    txtBuffer.tabs(1).textln(s"const long long _typeId() const;")
    txtBuffer.tabs(1).textln(s"const short _typeId16Bit() const;")
    txtBuffer.tabs(1).textln(s"const std::string& _typeId16BitBase64() const;")
    txtBuffer.tabs(1).textln(s"const std::string& _typeName() const;")
    txtBuffer.endl()
    
    txtBuffer.tabs(1).textln(s"const std::vector<long long>& _typeIds() const;")
    txtBuffer.tabs(1).textln(s"const std::vector<short>& _typeIds16Bit() const;")
    txtBuffer.tabs(1).textln(s"const std::vector<std::string>& _typeIds16BitBase64() const;")
    txtBuffer.tabs(1).textln(s"const std::vector<std::string>& _typeNames() const;")
    txtBuffer.endl()

    txtBuffer.tabs(1).textln(s"const std::vector<mgen::Field>& _fieldMetadatas() const;")
    txtBuffer.endl()

    txtBuffer.tabs(1).textln(s"bool _isFieldSet(const mgen::Field& field, const mgen::FieldSetDepth depth) const;")
    txtBuffer.endl()

  }

}