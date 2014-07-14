package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaSet

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils

object MkIncludes {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val unsorted = ((t.referencedClasses() - t) ++ t.referencedEnums())
    val referenced = unsorted.toSeq.sortBy(_.fullName)

    if (!t.hasSuperType())
      CppGenUtils.include("mgen/classes/MGenBase.h")

    for (tRef <- referenced)
      CppGenUtils.include(tRef)

    txtBuffer.endl()

  }

}