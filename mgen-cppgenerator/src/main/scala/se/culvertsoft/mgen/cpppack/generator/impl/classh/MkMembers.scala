package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkMembers {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields())
      txtBuffer
        .tabs(1)
        .textln(s"${getTypeName(field)} m_${field.name()};")

    for (field <- t.fields())
      txtBuffer.tabs(1).textln(s"bool ${isSetName(field)};")

    if (t.fields().nonEmpty)
      txtBuffer.endl()

  }

}