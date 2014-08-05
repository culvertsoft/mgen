package se.culvertsoft.mgen.cpppack.generator.impl.classh

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._

object MkHasers {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

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