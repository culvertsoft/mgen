package se.culvertsoft.mgen.cpppack.generator

import java.io.File

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

abstract class CppClassRegistryGenerator(artifactType: CppArtifactType)
  extends UtilityClassGenerator("ClassRegistry", Some("mgen::ClassRegistryBase"), artifactType) {

  override def mkClassContents(param: UtilClassGenParam) {

    mkDefaultCtor(param)
    mkDestructor(param)

    mkReadObjectFields(param)
    mkVisitObjectFields(param)

    mkGetByTypeIds16Bit(param)
    mkGetByTypeIds16BitBase64(param)

  }

  def mkDefaultCtor(param: UtilClassGenParam) {}

  def mkDestructor(param: UtilClassGenParam) {}

  def mkReadObjectFields(param: UtilClassGenParam) {}

  def mkVisitObjectFields(param: UtilClassGenParam) {}

  def mkGetByTypeIds16Bit(param: UtilClassGenParam) {}

  def mkGetByTypeIds16BitBase64(param: UtilClassGenParam) {}

}