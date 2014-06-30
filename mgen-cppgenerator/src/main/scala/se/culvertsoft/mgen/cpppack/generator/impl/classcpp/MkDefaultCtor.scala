package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppConstruction._
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.canBeNull
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.writeInitializerList
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkDefaultCtor {

  def mkInitializerList(fields: Seq[Field], module: Module)(implicit txtBuffer: SuperStringBuffer) {
    implicit val currentModule = module

    val initializerList = new ArrayBuffer[String]
    initializerList ++= fields map (f => s"m_${f.name()}(${defaultConstructNull(f)})")
    initializerList ++= fields map (f => s"${isSetName(f)}(false)")
    writeInitializerList(initializerList)
  }

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    txt(s"${t.name()}::${t.name()}()")
    if (t.fields.nonEmpty)
      mkInitializerList(t.fields.filterNot(canBeNull), module)
    ln(" {")
    ln("}")
    endl()

  }

}