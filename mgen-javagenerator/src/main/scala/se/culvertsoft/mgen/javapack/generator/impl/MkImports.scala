package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.deepCopyerClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.enumImplClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.eqTesterClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldHasherClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldVisitorClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.readerClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.setFieldSetClsStringQ
import se.culvertsoft.mgen.javapack.generator.JavaConstants.validatorClsStringQ

object MkImports {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    val extClasses = t.referencedClasses().filter(_.module != module)
    val extEnums = t.referencedEnums().filter(_.module != module)

    ln(s"import ${fieldClsStringQ};")

    for (e <- extClasses)
      ln(s"import ${e.module.path}.${e.shortName};")

    for (e <- extEnums)
      ln(s"import ${e.module.path}.${e.shortName};")

    ln(s"import ${fieldSetDepthClsStringQ};")
    ln(s"import ${fieldVisitorClsStringQ};")
    ln(s"import ${readerClsStringQ};")

    if (t.fieldsInclSuper().nonEmpty) {
      ln(s"import ${eqTesterClsStringQ};")
      ln(s"import ${deepCopyerClsStringQ};")
      ln(s"import ${fieldHasherClsStringQ};")
    }

    if (t.fields().exists(_.typ().containsCustomType())) {
      ln(s"import ${validatorClsStringQ};")
      ln(s"import ${setFieldSetClsStringQ};")
    }

    endl()

  }

}