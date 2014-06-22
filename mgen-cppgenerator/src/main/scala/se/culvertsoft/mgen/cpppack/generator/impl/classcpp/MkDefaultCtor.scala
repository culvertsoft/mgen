package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.CppConstruction

import se.culvertsoft.mgen.cpppack.generator.impl.Alias._

object MkDefaultCtor {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {
    
    implicit val currentModule = module

    txtBuffer.tabs(0).text(s"${t.name()}::${t.name()}()")

    if (t.fields().nonEmpty) {
      txtBuffer.textln(" : ")
      for ((field, i) <- t.fields().zipWithIndex) {
        txtBuffer.tabs(2).textln(s"m_${field.name()}(${CppConstruction.defaultConstruct(field)}),")
      }

      for ((field, i) <- t.fields().zipWithIndex) {
        txtBuffer.tabs(2).text(s"${isSetName(field)}(false)")
        if (i + 1 < t.fields().size())
          txtBuffer.comma().endl()
      }
    }
    txtBuffer.tabs(0).textln(" {")
    txtBuffer.tabs(0).textln("}").endl()


  }
  
}