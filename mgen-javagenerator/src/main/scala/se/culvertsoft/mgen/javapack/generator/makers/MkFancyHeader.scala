package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants

object MkFancyHeader {
  import BuiltInGeneratorUtil._
  import JavaConstants._

  def apply(t: CustomType)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(fileHeader);
  }
  
  def MkMetadataMethodsComment(t: CustomType)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(serializationSectionHeader).endl();
  }

  def MkMetadataComment(t: CustomType)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(metadataSectionHeader).endl();
  }
}