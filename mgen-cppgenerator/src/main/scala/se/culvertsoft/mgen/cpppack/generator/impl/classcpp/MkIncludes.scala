package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppGenerator

object MkIncludes {

  def apply(t: ClassType, genCustomCodeSections: Boolean)(implicit txtBuffer: SourceCodeBuffer) {

    CppGenUtils.include(t)
    CppGenUtils.include("mgen/util/validation.h")
    CppGenUtils.include("mgen/util/stlLiteral.h")

    if (genCustomCodeSections)
      ln(CppGenerator.custom_includes_section.toString)

    txtBuffer.endl()

  }

}