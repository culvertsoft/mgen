package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isSetName
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkDefaultCtor {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    ln(1, s"public ${t.shortName}() {")
    ln(2, s"super();");
    for (field <- t.fields()) {
      ln(2, s"m_${field.name()} = ${MkDefaultValue(field, false)};")
    }
    for (field <- t.fields()) {
      if (!JavaGenerator.canBeNull(field))
        ln(2, s"${isSetName(field)} = ${field.hasDefaultValue};")
    }
    ln(1, "}")
    endl()

  }

}