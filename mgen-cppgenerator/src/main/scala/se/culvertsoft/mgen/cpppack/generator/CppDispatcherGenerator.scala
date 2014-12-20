package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

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

  override def mkClassContents(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    mkDispatch(param)
  }

  override def mkClassStart(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  override def mkClassEnd(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  def mkDispatch(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

}