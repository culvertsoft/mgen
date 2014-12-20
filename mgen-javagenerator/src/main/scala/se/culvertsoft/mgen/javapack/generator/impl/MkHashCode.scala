package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldHasherClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkHashCode {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()
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