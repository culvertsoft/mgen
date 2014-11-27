package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
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
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkImports {

  def apply(t: ClassType, module: Module, genCustomCodeSections: Boolean)(implicit txtBuffer: SuperStringBuffer) {

    ln(s"import ${fieldIfcClsStringQ};")

    ln(s"import ${fieldSetDepthClsStringQ};")
    ln(s"import ${fieldVisitSelectionClsStringQ};")
    ln(s"import ${fieldVisitorClsStringQ};")
    ln(s"import ${readerClsStringQ};")

    if (t.fieldsInclSuper.nonEmpty) {
      ln(s"import ${fieldHasherClsStringQ};")
    }

    if (t.fieldsInclSuper.filter(JavaGenerator.isMutable).nonEmpty) {
      ln(s"import ${deepCopyerClsStringQ};")
    }

    if (t.fieldsInclSuper.filter(JavaGenerator.needsDeepEqual).nonEmpty) {
      ln(s"import ${eqTesterClsStringQ};")
    }
    
    if (t.fields.exists(_.typ.containsUserDefinedType)) {
      ln(s"import ${validatorClsStringQ};")
      ln(s"import ${setFieldSetClsStringQ};")
    }

    if (genCustomCodeSections)
      ln(JavaGenerator.custom_imports_section.toString)

    endl()

  }

}