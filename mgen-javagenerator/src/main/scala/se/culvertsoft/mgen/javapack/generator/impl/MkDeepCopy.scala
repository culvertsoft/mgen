package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.get
import Alias.getCopy
import Alias.has
import Alias.unset
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaGenerator.canBeNull
import se.culvertsoft.mgen.javapack.generator.JavaGenerator.isMutable

object MkDeepCopy {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    def deepCopyField(f: Field): String = {
      if (isMutable(f)) {
        getCopy(f)
      } else {
        get(f)
      }
    }

    val allFields = t.fieldsInclSuper()
    val allFieldsStrings = allFields.map(deepCopyField)
    val clsName = t.shortName;

    ln(1, "@Override")
    ln(1, s"public ${t.shortName()} deepCopy() {")

    if (allFieldsStrings.isEmpty) {
      ln(2, s"return new $clsName();")
    } else {
      ln(2, s"final $clsName out = new $clsName(")
      for (f <- allFieldsStrings) {
        txt(3, s"$f")
        if (!(f eq allFieldsStrings.last)) {
          ln(",")
        }
      }
      ln(");")
      for (f <- allFields) {
        if (!canBeNull(f)) {
          ln(2, s"if (!${has(f)}) out.${unset(f)};")
        }
      }
      ln(2, "return out;")
    }
    ln(1, "}").endl()

  }
}