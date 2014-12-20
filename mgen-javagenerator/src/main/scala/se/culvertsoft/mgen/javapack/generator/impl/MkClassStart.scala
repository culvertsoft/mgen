package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkClassStart {

  def apply(clsName: String, superTypeName: String, genCustomCodeSections: Boolean = false)(implicit txtBuffer: SourceCodeBuffer) {
    val customSectionString = if (genCustomCodeSections) JavaGenerator.custom_interfaces_section else ""

    if (superTypeName != null && superTypeName.nonEmpty)
      txtBuffer.text(s"public class $clsName extends $superTypeName ${customSectionString} {").endl2()
    else
      txtBuffer.text(s"public class $clsName ${customSectionString} {").endl2()
  }

  def apply(t: ClassType, module: Module, genCustomCodeSections: Boolean)(implicit txtBuffer: SourceCodeBuffer) {
    apply(t.shortName, getTypeName(t.superType())(module), genCustomCodeSections)
  }

}