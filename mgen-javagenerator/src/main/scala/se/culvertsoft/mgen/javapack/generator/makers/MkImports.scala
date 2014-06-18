package se.culvertsoft.mgen.javapack.generator.makers

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.asScalaSet

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.deepCopyerClsStringQ
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
    val allReferencedExtTypes = t.getAllReferencedExtTypesInclSuper()
    txtBuffer.textln(s"import ${fieldClsStringQ};")
    for (referencedExtType <- allReferencedExtTypes)
      txtBuffer.textln(s"import ${referencedExtType.module().path()};")
    //txtBuffer.textln(s"import ${fieldDefValueClsStringQ};")
    txtBuffer.textln(s"import ${fieldSetDepthClsStringQ};")
    txtBuffer.textln(s"import ${fieldVisitorClsStringQ};")
    txtBuffer.textln(s"import ${readerClsStringQ};")
    if (t.getAllFieldsInclSuper().nonEmpty) {
      txtBuffer.textln(s"import ${eqTesterClsStringQ};")
      txtBuffer.textln(s"import ${deepCopyerClsStringQ};")
      txtBuffer.textln(s"import ${fieldHasherClsStringQ};")
    }
    if (t.fields().exists(_.typ().containsMgenCreatedType())) {
      txtBuffer.textln(s"import ${validatorClsStringQ};")
      txtBuffer.textln(s"import ${setFieldSetClsStringQ};")
    }
    txtBuffer.endl()
  }

}