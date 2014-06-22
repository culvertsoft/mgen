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

object MkFieldById {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.getAllFieldsInclSuper()
    
    txtBuffer.tabs(0).textln(s"const mgen::Field * ${t.shortName()}::_fieldById(const short hash) const {")
    txtBuffer.tabs(1).textln(s"switch (hash) {")
    for (field <- allFields) {
      txtBuffer.tabs(1).textln(s"case ${fieldIdString(field)}:")
      txtBuffer.tabs(2).textln(s"return &${fieldMetaString(field)};")
    }
    txtBuffer.tabs(1).textln(s"default:")
    txtBuffer.tabs(2).textln(s"return 0;");
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.tabs(0).textln(s"}")
    txtBuffer.endl()

  }

}