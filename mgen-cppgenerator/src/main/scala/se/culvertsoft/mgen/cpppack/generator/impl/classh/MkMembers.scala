package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkMembers {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val module = t.module

    for (field <- t.fields())
      ln(1, s"${getTypeName(field)} m_${field.name()};")

    for (field <- t.fields())
      if (!CppGenerator.canBeNull(field))
        ln(1, s"bool ${isSetName(field)};")

    if (t.fields().nonEmpty)
      endl()

  }

}