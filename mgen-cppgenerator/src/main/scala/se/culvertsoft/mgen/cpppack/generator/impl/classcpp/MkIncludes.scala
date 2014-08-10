package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppGenerator

object MkIncludes {

  def apply(
    t: ClassType,
    module: Module,
    genCustomCodeSections: Boolean)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    CppGenUtils.include(t)
    CppGenUtils.include("mgen/util/validation.h")
    CppGenUtils.include("mgen/util/stlLiteral.h")

    if (genCustomCodeSections)
      ln(CppGenerator.custom_includes_section.toString)

    txtBuffer.endl()

  }

}