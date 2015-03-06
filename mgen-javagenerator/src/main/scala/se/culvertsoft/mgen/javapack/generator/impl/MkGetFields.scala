package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldIfcClsString

object MkGetFields {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    ln(1, "@Override")
    ln(1, s"public $fieldIfcClsString[] _fields() {")
    ln(2, s"return _FIELDS;")
    ln(1, "}").endl()

  }
}