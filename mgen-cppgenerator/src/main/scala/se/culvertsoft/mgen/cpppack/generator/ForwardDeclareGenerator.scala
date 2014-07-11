package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln

object ForwardDeclareGenerator extends UtilityClassGenerator("ForwardDeclare", None, Header) {

  override def mkClassStart(param: UtilClassGenParam) {}

  override def mkClassEnd(param: UtilClassGenParam) {}

  override def mkNamespaceStart(param: UtilClassGenParam) {}

  override def mkNamespaceEnd(param: UtilClassGenParam) {}

  override def mkIncludes(param: UtilClassGenParam) {
    super.mkIncludes(param)
    endl()

    for (m <- param.modules) {
      val namespaces = m.path.split("\\.")
      CppGenUtils.mkNameSpaces(namespaces)
      for (t <- m.types)
        ln(s"class ${t.shortName};")
      endl()
      CppGenUtils.mkNameSpacesEnd(namespaces)
    }
    endl()

  }

  def includeStringH(namespaceString: String): String = {
    s"$namespaceString::ForwardDeclare.h".replaceAllLiterally("::", "/")
  }

}