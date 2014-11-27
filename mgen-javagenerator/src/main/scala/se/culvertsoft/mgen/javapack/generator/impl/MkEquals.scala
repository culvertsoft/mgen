package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import Alias.fieldMetadata
import Alias.has
import Alias.get
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.eqTesterClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.javapack.generator.JavaGenerator

object MkEquals {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    BuiltInGeneratorUtil

    val allFields = t.fieldsInclSuper()

    ln(1, "@Override")
    ln(1, "public boolean equals(final Object other) {")
    ln(2, s"if (other == null) return false;")
    ln(2, s"if (other == this) return true;")
    ln(2, s"if (${t.shortName}.class != other.getClass()) return false;")

    if (allFields.nonEmpty)
      ln(2, s"final ${t.shortName} o = (${t.shortName})other;")
    txt(2, "return true")
    
    for (field <- allFields) {
      ln()
      txt(2, s"  && ${has(field)} == o.${has(field)}")
    }
    
    for (field <- allFields) {
      ln()
      if (JavaGenerator.needsDeepEqual(field)) {
        txt(2, s"  && ${eqTesterClsString}.areEqual(${get(field)}, o.${get(field)}, ${fieldMetadata(field)}.typ())")  
      } else {
        txt(2, s"  && ${get(field)} == o.${get(field)}")
      }      
    }
    
    txtBuffer.textln(";")
    ln(1, "}").endl()
  }
}