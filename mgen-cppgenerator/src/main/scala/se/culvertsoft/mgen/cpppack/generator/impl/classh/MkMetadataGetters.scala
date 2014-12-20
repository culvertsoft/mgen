package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkMetadataGetters {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

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