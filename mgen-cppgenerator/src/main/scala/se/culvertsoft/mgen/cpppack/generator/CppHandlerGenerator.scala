package se.culvertsoft.mgen.cpppack.generator

object CppHandlerGenerator {
  def includeStringH(namespaceString: String): String = {
    s"$namespaceString::Handler.h".replaceAllLiterally("::", "/")
  }
  def includeStringCpp(namespaceString: String): String = {
    s"$namespaceString::Handler.cpp".replaceAllLiterally("::", "/")
  }
}

abstract class CppHandlerGenerator(artifactType: CppArtifactType)
  extends UtilityClassGenerator("Handler", None, artifactType) {

  override def mkClassContents(param: UtilClassGenParam) {
  }

}
