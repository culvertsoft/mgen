package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.deepCopyerClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.eqTesterClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldHasherClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldIfcClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldVisitSelectionClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldVisitorClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.readerClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.setFieldSetClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.validatorClsStringQ

object MkImports {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    ln(s"import ${fieldIfcClsStringQ};")

    ln(s"import ${fieldSetDepthClsStringQ};")
    ln(s"import ${fieldVisitSelectionClsStringQ};")
    ln(s"import ${fieldVisitorClsStringQ};")
    ln(s"import ${readerClsStringQ};")

    if (t.fieldsInclSuper.nonEmpty) {
      ln(s"import ${eqTesterClsStringQ};")
      ln(s"import ${deepCopyerClsStringQ};")
      ln(s"import ${fieldHasherClsStringQ};")
    }

    if (t.fields.exists(_.typ.containsUserDefinedType)) {
      ln(s"import ${validatorClsStringQ};")
      ln(s"import ${setFieldSetClsStringQ};")
    }

    endl()

  }

}