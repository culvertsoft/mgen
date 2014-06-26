package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkMetadataGetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    ln(1, s"const long long _typeId() const;")
    ln(1, s"const short _typeId16Bit() const;")
    ln(1, s"const std::string& _typeId16BitBase64() const;")
    ln(1, s"const std::string& _typeName() const;")
    endl()
    
    ln(1, s"const std::vector<long long>& _typeIds() const;")
    ln(1, s"const std::vector<short>& _typeIds16Bit() const;")
    ln(1, s"const std::vector<std::string>& _typeIds16BitBase64() const;")
    ln(1, s"const std::string& _typeIds16BitBase64String() const;")
    ln(1, s"const std::vector<std::string>& _typeNames() const;")
    endl()

    ln(1, s"const std::vector<mgen::Field>& _fieldMetadatas() const;")
    endl()

    ln(1, s"bool _isFieldSet(const mgen::Field& field, const mgen::FieldSetDepth depth) const;")
    endl()

  }

}