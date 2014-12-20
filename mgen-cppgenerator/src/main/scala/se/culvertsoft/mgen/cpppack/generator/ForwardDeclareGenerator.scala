package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object ForwardDeclareGenerator extends UtilityClassGenerator("ForwardDeclare", None, Header) {

  override def mkClassStart(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  override def mkClassEnd(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  override def mkNamespaceStart(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  override def mkNamespaceEnd(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {}

  override def mkIncludes(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    super.mkIncludes(param)
    endl()

    for (m <- param.modules) {
      val namespaces = m.path.split("\\.")
      CppGenUtils.mkNameSpaces(namespaces)
      for (t <- m.classes)
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