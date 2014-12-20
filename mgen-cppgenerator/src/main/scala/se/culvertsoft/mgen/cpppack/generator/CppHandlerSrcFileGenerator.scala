package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkLongTypeName

object CppHandlerSrcFileGenerator extends CppHandlerGenerator(SrcFile) {

  override def mkIncludes(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.include("Handler.h")
    val classes = param.modules.flatMap(_.classes).distinct.filterNot(_.hasSubTypes())
    for (t <- classes)
      CppGenUtils.include(t)

    endl()
  }

  override def mkClassContents(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    super.mkClassContents(param)

    val allClasses = param.modules.flatMap(_.classes)
    val topLevelClasses = allClasses.filterNot(_.hasSuperType())

    ln("Handler::Handler() {}").endl()
    ln("Handler::~Handler() {}").endl()

    def mkDefaultHandlers() {
      ln("void Handler::handleDiscard(mgen::MGenBase& o) {")
      ln("}").endl()
      ln("void Handler::handleUnknown(mgen::MGenBase& o) {")
      ln(1, s"handleDiscard(o);")
      ln("}").endl()
    }

    def mkHandler(t: ClassType) {
      val passCall = (if (t.hasSuperType()) s"handle(static_cast<${MkLongTypeName.cpp(t.superType.asInstanceOf[ClassType])}&>(o))" else "handleDiscard(o)")
      ln(s"void Handler::handle(${MkLongTypeName.cpp(t)}& o) {")
      ln(1, s"$passCall;")
      ln("}")
      endl()
    }

    def mkHandlers() {
      allClasses foreach mkHandler
    }

    mkDefaultHandlers()
    mkHandlers()

    endl()

  }

}