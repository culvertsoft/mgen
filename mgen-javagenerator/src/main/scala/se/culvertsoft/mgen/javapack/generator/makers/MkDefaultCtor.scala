package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.javapack.generator.JavaConstruction._
import se.culvertsoft.mgen.api.model.Module

object MkDefaultCtor {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {
    
    implicit val m = module
    
    txtBuffer.tabs(1).textln(s"public ${t.name()}() {")
    txtBuffer.tabs(2).textln(s"super();");
    for (field <- t.fields()) {
      txtBuffer.tabs(2).textln(s"m_${field.name()} = ${defaultConstructNull(field.typ())};")
    }
    for (field <- t.fields()) {
      txtBuffer.tabs(2).textln(s"${isSetName(field)} = false;")
    }
    txtBuffer.tabs(1).textln("}")
    txtBuffer.endl()

  }

}