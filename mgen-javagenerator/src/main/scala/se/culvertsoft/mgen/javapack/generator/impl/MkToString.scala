package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.get
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaToString

object MkToString {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val fields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln("@Override")

    txtBuffer.tabs(1).textln("public String toString() {")

    if (fields.nonEmpty) {
      txtBuffer.tabs(2).textln("final java.lang.StringBuilder sb = new java.lang.StringBuilder();")

      txtBuffer.tabs(2).text("sb.append(\"").text(t.fullName()).textln(":\\n\");")

      for ((field, i) <- fields.zipWithIndex) {
        txtBuffer.tabs(2)
          .text("sb.append(\"  \")")
          .text(".append(\"")
          .text(field.name())
          .text(" = \")")
          .text(s".append(${JavaToString.mkToString(field.typ())(s"${get(field)}")})")
        if (i + 1 < fields.size())
          txtBuffer.text(".append(\"\\n\");")
        else
          txtBuffer.text(";")
        txtBuffer.endl()
      }
      txtBuffer.tabs(2).textln("return sb.toString();")
    } else {
      txtBuffer.tabs(2).textln("return _typeName() + \"_instance\";")
    }

    txtBuffer.tabs(1).textln("}").endl()

  }
}