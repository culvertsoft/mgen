package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import Alias.isSetName
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstruction.defaultConstructNull
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkDefaultCtor {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    txtBuffer.tabs(1).textln(s"public ${t.name()}() {")
    txtBuffer.tabs(2).textln(s"super();");
    for (field <- t.fields()) {
      txtBuffer.tabs(2).textln(s"m_${field.name()} = ${defaultConstructNull(field.typ())};")
    }
    for (field <- t.fields()) {
      if (!JavaGenerator.canBeNull(field))
        txtBuffer.tabs(2).textln(s"${isSetName(field)} = false;")
    }
    txtBuffer.tabs(1).textln("}")
    txtBuffer.endl()

  }

}