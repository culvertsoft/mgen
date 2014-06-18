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

object MkHashCode {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()
    val hashBase = t.fullName().hashCode()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public int hashCode() {")

    if (allFields.nonEmpty) {
      txtBuffer.tabs(2).textln(s"final int prime = 31;")
      txtBuffer.tabs(2).textln(s"int result = ${hashBase};")
      for (f <- allFields) {
        txtBuffer.tabs(2).textln(s"result = ${isFieldSet(f, s"${fieldSetDepthClsString}.SHALLOW")} ? (prime * result + ${fieldHasherClsString}.calc(${get(f)}, ${fieldMetadata(f)}.typ())) : result;")
      }
      txtBuffer.tabs(2).textln(s"return result;")
    } else {
      txtBuffer.tabs(2).textln(s"return ${hashBase};")
    }

    txtBuffer.tabs(1).textln("}").endl()

  }
}