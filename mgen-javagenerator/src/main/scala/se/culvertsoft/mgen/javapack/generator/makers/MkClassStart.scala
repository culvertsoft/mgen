package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkClassStart {

  def apply(clsName: String, superTypeName: String)(implicit txtBuffer: SuperStringBuffer) {
    if (superTypeName != null && superTypeName.nonEmpty)
      txtBuffer.text(s"public class $clsName extends $superTypeName {").endl2()
    else
      txtBuffer.text(s"public class $clsName {").endl2()
  }

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {
    apply(t.name(), getTypeName(t.superType())(module))
  }

}