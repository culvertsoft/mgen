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
import se.culvertsoft.mgen.cpppack.generator.CppGenerator

object MkGetters {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields()) {
      txtBuffer.tabs(0).textln(s"const ${getTypeName(field)}& ${t.shortName()}::${get(field)} const {")
      txtBuffer.tabs(1).textln(s"return m_${field.name()};")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

    for (field <- t.fields()) {
      txtBuffer.tabs(0).textln(s"${getTypeName(field)}& ${t.shortName()}::${getMutable(field)} {")
      if (!CppGenerator.canBeNull(field))
        txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
      txtBuffer.tabs(1).textln(s"return m_${field.name()};")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

  }

}