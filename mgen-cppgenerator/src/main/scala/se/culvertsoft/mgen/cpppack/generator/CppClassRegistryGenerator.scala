package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

abstract class CppClassRegistryGenerator(artifactType: CppArtifactType)
  extends UtilityClassGenerator("ClassRegistry", Some("mgen::ClassRegistryBase"), artifactType) {

  override def mkClassContents(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {

    mkDefaultCtor(param)
    mkDestructor(param)

    mkReadObjectFields(param)
    mkVisitObjectFields(param)

    mkGetByTypeIds16Bit(param)
    mkGetByTypeIds16BitBase64(param)

  }

  def mkDefaultCtor(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkDestructor(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkReadObjectFields(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkVisitObjectFields(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkGetByTypeIds16Bit(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkGetByTypeIds16BitBase64(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

}