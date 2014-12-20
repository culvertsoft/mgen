package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Constant
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.FixedPointType
import se.culvertsoft.mgen.api.model.FloatingPointType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName

object MkConstants {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val module = t.module

    val constants = t.constants.filter(isConstantInCppFile)

    if (constants.nonEmpty) {
      for (c <- constants) {
        ln(0, s"const ${getTypeName(c.source)}& ${t.shortName}::${c.shortName}() {")
        ln(1, s"static const ${getTypeName(c.source)} out = ${MkDefaultValue(c.value, c.source.isPolymorphic)};")
        ln(1, s"return out;")
        ln(0, s"}")
        ln()
      }
    }

  }

  def isConstantInCppFile(c: Constant): Boolean = {
    c.typ match {
      case t: EnumType => false
      case t: FixedPointType => false
      case t: FloatingPointType => false
      case _ => true
    }
  }

}