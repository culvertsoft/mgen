package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames
import se.culvertsoft.mgen.api.model.Module

import scala.collection.JavaConversions._

object MkImports {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import JavaTypeNames._

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