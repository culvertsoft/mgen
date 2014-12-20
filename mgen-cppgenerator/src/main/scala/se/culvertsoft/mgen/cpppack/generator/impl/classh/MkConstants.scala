package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.FixedPointType
import se.culvertsoft.mgen.api.model.FloatingPointType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDefaultValue
import se.culvertsoft.mgen.idlgenerator.util.IdlGenUtil

object MkConstants {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val module = t.module

    if (t.constants.nonEmpty) {
      ln(0, "public:")
      for (c <- t.constants) {
        c.typ match {
          case t: EnumType =>
            ln(1, s"static const ${getTypeName(c.source)} ${c.shortName} = ${MkDefaultValue(c.value, false)};")
          case t: FixedPointType =>
            ln(1, s"static const ${getTypeName(c.source)} ${c.shortName} = ${MkDefaultValue(c.value, false)};")
          case t: FloatingPointType =>
            ln(1, s"static const ${getTypeName(c.source)} ${c.shortName}() { return ${MkDefaultValue(c.value, false)}; };")
          case _ =>
            ln(1, s"static const ${getTypeName(c.source)}& ${c.shortName}(); // ${IdlGenUtil.defaultVal2String(c.value)}")
        }
      }
      ln()
    }

  }

}