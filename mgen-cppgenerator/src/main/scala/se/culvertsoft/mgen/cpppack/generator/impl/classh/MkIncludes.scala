package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaSet

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
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

    val unsorted = ((t.referencedClasses() - t) ++ t.referencedEnums())
    val referenced = unsorted.toSeq.sortBy(_.fullName)

    if (!t.hasSuperType())
      CppGenUtils.include("mgen/classes/MGenBase.h")

    for (tRef <- referenced)
      CppGenUtils.include(tRef)

    if (genCustomCodeSections)
      ln(CppGenerator.custom_includes_section.toString)

    endl()

  }

}