package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkHasers {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    for (field <- t.fields())
      ln(1, s"bool has${upFirst(field.name)}() const;")

    if (t.fields().nonEmpty)
      ln("")

    val allFields = t.fieldsInclSuper()

    for (field <- allFields)
      ln(1, s"${t.shortName}& unset${upFirst(field.name)}();")

    if (allFields.nonEmpty)
      ln("")

  }

}