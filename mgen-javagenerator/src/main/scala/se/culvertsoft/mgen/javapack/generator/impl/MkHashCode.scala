package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldHasherClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString

object MkHashCode {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()
    val hashBase = t.fullName().hashCode()

    ln(1, "@Override")
    ln(1, "public int hashCode() {")

    if (allFields.nonEmpty) {
      ln(2, s"final int prime = 31;")
      ln(2, s"int result = ${hashBase};")
      for (f <- allFields) {
        ln(2, s"result = ${isFieldSet(f, s"${fieldSetDepthClsString}.SHALLOW")} ? (prime * result + ${fieldHasherClsString}.calc(${get(f)}, ${fieldMetadata(f)}.typ())) : result;")
      }
      ln(2, s"return result;")
    } else {
      ln(2, s"return ${hashBase};")
    }

    ln(1, "}").endl()

  }
}