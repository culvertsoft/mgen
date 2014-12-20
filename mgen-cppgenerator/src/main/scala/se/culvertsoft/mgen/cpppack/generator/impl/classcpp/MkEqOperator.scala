package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.get
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet

object MkEqOperator {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    val allFields = t.fieldsInclSuper()
    ln(s"bool ${t.shortName()}::operator==(const ${t.shortName()}& other) const {")
    if (allFields.nonEmpty) {
      ln(1, "return true")
      for (field <- allFields) {
        ln(2, s" && ${isFieldSet(field, "mgen::SHALLOW")} == other.${isFieldSet(field, "mgen::SHALLOW")}")
      }
      for (field <- allFields) {
        txt(2, s" && ${get(field)} == other.${get(field)}")
        if (field != allFields.last)
          endl()
      }
      ln(";")
    } else {
      ln(1, "return true;")
    }
    ln(s"}")
    endl()

    ln(s"bool ${t.shortName()}::operator!=(const ${t.shortName()}& other) const {")
    ln(1, "return !(*this == other);")
    ln(s"}")
    endl()

  }

}