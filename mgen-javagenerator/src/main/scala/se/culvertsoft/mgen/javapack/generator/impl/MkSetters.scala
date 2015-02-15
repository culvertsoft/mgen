package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isSetName
import Alias.set
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.declared

object MkSetters {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val thisFields = t.fields()
    val superFields = t.fieldsInclSuper() -- thisFields

    for (field <- superFields) {
      ln(1, s"public ${t.shortName} ${set(field, s"final ${declared(field)} ${field.name()}")} {")
      ln(2, s"super.${set(field, field.name())};")
      ln(2, s"return this;")
      ln(1, s"}").endl()
    }

    for (field <- thisFields) {
      ln(1, s"public ${t.shortName} ${set(field, s"final ${declared(field)} ${field.name()}")} {")
      ln(2, s"m_${field.name()} = ${field.name()};")
      if (!JavaGenerator.canBeNull(field))
        ln(2, s"${isSetName(field)} = true;")
      ln(2, s"return this;")
      ln(1, s"}").endl()
    }

  }
}
