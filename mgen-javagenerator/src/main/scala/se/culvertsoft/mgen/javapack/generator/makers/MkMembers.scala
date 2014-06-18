package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isSetName
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkMembers {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fields()
    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"private ${getTypeName(field.typ())} m_${field.name()};")
    }

    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"private boolean ${isSetName(field)};")
    }

    if (fields.nonEmpty)
      txtBuffer.endl()

  }

}