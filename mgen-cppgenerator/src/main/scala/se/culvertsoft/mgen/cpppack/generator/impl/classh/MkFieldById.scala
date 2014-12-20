package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkFieldById {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    ln(1, s"const mgen::Field * _fieldById(const short id) const;")
    ln(1, s"const mgen::Field * _fieldByName(const std::string& name) const;")
    
  }

}