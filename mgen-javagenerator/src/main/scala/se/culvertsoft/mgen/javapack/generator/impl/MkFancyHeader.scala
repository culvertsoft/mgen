package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fileHeader
import se.culvertsoft.mgen.javapack.generator.JavaConstants.metadataSectionHeader
import se.culvertsoft.mgen.javapack.generator.JavaConstants.serializationSectionHeader

object MkFancyHeader {

  def apply(t: ClassType)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(fileHeader);
  }

  def MkMetadataMethodsComment(t: ClassType)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(serializationSectionHeader).endl();
  }

  def MkMetadataComment(t: ClassType)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(metadataSectionHeader).endl();
  }
}