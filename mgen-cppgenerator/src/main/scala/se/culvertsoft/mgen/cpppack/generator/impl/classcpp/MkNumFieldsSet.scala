package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames._

object MkNumFieldsSet {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(0).textln(s"int ${t.shortName()}::_numFieldsSet(const mgen::FieldSetDepth depth) const {")
    txtBuffer.tabs(1).textln(s"int out = 0;")
    for (field <- allFields)
      txtBuffer.tabs(1).textln(s"out += ${isFieldSet(field, "depth")} ? 1 : 0;")
    txtBuffer.tabs(1).textln(s"return out;")
    txtBuffer.tabs(0).textln("}").endl()

  }

}