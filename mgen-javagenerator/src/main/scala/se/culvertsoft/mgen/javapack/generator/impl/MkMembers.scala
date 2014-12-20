package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isSetName
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkMembers {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    for (c <- t.constants)
      ln(1, s"public static final ${getTypeName(c.typ)} ${c.shortName} = ${MkDefaultValue(c.value, true)};")
    if (t.constants.nonEmpty)
      ln()

    val fields = t.fields()
    for (field <- fields) {
      ln(1, s"private ${getTypeName(field.typ())} m_${field.name()};")
    }

    for (field <- fields) {
      if (!JavaGenerator.canBeNull(field.typ()))
        ln(1, s"private boolean ${isSetName(field)};")
    }

    if (fields.nonEmpty)
      endl()

  }

}