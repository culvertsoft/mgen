package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames
import se.culvertsoft.mgen.api.model.Module

import scala.collection.JavaConversions._

object MkMembers {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import JavaTypeNames._
  import Alias._

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