package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaSet

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils

object MkIncludes {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val types = t.getDirectDependencies() - t
    if (t.superType().typeEnum() == TypeEnum.MGEN_BASE)
      CppGenUtils.include("mgen/classes/MGenBase.h")
    for (tRef <- types)
      CppGenUtils.include(tRef)
    txtBuffer.endl()

  }

}