package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.isSetName
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkMembers {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fields()
    for (field <- fields) {
      txtBuffer.tabs(1).textln(s"private ${getTypeName(field.typ())} m_${field.name()};")
    }

    for (field <- fields) {
      if (!JavaGenerator.canBeNull(field.typ()))
        txtBuffer.tabs(1).textln(s"private boolean ${isSetName(field)};")
    }

    if (fields.nonEmpty)
      txtBuffer.endl()

  }

}