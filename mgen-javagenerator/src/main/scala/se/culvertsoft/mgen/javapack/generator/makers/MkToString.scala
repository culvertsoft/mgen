package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.javapack.generator.JavaConstruction._
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames._
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.javapack.generator.JavaToString

object MkToString {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {
    
    implicit val m = module
    
    val fields = t.getAllFieldsInclSuper()
    
    txtBuffer.tabs(1).textln("@Override")

    txtBuffer.tabs(1).textln("public String toString() {")

    if (fields.nonEmpty) {
      txtBuffer.tabs(2).textln("final java.lang.StringBuffer sb = new java.lang.StringBuffer();")

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