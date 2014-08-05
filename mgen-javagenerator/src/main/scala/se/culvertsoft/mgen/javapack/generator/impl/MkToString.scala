package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants

object MkToString {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fieldsInclSuper()

    ln(1, "@Override")
    ln(1, "public String toString() {")
    ln(2, s"return ${JavaConstants.stringifyerClsQ}.toString(this);")

    ln(1, "}").endl()

  }
}