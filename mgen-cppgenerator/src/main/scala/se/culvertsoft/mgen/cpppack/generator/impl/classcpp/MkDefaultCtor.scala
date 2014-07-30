package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.canBeNull
import se.culvertsoft.mgen.cpppack.generator.CppGenerator.writeInitializerList
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName
import se.culvertsoft.mgen.cpppack.generator.impl.HasDefaultCtor

object MkDefaultCtor {

  def needsInitializerListValue(field: Field): Boolean = {
    !HasDefaultCtor(field) || field.hasDefaultValue
  }

  def mkInitializeListValue(field: Field, module: Module): String = {
    MkDefaultValue.apply(field)(module)
  }

  def mkInitializerList(fields: Seq[Field], module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val initializerList = new ArrayBuffer[String]
    initializerList ++= fields.filter(needsInitializerListValue) map (f => s"m_${f.name()}(${MkDefaultValue.apply(f)(module)})")
    initializerList ++= fields.filterNot(canBeNull) map (f => s"${isSetName(f)}(${f.hasDefaultValue})")
    writeInitializerList(initializerList)
  }

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    txt(s"${t.name()}::${t.name()}()")
    if (t.fields.nonEmpty)
      mkInitializerList(t.fields, module)
    ln(" {")
    ln("}")
    endl()

  }

}