package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldClsString

object MkGetFields {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public $fieldClsString[] _fields() {")
    txtBuffer.tabs(2).textln(s"return _FIELDS;")
    txtBuffer.tabs(1).textln("}").endl()

  }
}