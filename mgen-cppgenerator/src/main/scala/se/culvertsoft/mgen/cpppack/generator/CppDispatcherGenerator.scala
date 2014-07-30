package se.culvertsoft.mgen.cpppack.generator

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