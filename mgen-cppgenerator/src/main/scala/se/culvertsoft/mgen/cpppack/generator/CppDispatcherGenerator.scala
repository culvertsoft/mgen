package se.culvertsoft.mgen.cpppack.generator

import java.io.File
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object CppDispatchGenerator {
  def includeStringH(namespaceString: String): String = {
    s"$namespaceString::Dispatcher.h".replaceAllLiterally("::", "/")
  }
  def includeStringCpp(namespaceString: String): String = {
    s"$namespaceString::Dispatcher.cpp".replaceAllLiterally("::", "/")
  }
}

abstract class CppDispatchGenerator(artifactType: CppArtifactType)
  extends UtilityClassGenerator("Dispatcher", None, artifactType) {

  override def mkClassContents(param: UtilClassGenParam) {
    mkDispatch(param)
  }

  override def mkClassStart(param: UtilClassGenParam) {}

  override def mkClassEnd(param: UtilClassGenParam) {}

  def mkDispatch(param: UtilClassGenParam) {}

}