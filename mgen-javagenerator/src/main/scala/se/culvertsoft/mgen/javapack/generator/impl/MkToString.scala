package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants

object MkToString {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.fieldsInclSuper()

    ln(1, "@Override")
    ln(1, "public String toString() {")
    ln(2, s"return ${JavaConstants.stringifyerClsQ}.toString(this);")

    /*
    if (fields.nonEmpty) {
      ln(2, "final java.lang.StringBuilder sb = new java.lang.StringBuilder();")

      ln(2, "sb.append(\"").text(t.fullName()).textln(":\\n\");")

      for ((field, i) <- fields.zipWithIndex) {
        txt(2, "sb.append(\"  \")")
          .text(".append(\"")
          .text(field.name())
          .text(" = \")")
          .text(s".append(${JavaToString.mkToString(field.typ())(s"${get(field)}")})")
        if (i + 1 < fields.size())
          txt(".append(\"\\n\");")
        else
          txt(";")
        endl()
      }
      ln(2, "return sb.toString();")
    } else {
      ln(2, "return _typeName() + \"_instance\";")
    }
*/
    ln(1, "}").endl()

  }
}