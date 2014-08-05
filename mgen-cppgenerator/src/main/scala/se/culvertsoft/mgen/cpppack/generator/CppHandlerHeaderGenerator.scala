package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkLongTypeName

object CppHandlerHeaderGenerator extends CppHandlerGenerator(Header) {

  override def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include("ForwardDeclare.h")
    endl()
  }

  override def mkClassContents(param: UtilClassGenParam) {
    super.mkClassContents(param)
    ln(1, "public:")
    endl()

    val allClasses = param.modules.flatMap(_.classes)
    val topLevelClasses = allClasses.filterNot(_.hasSuperType())

    ln(1, s"Handler();")
    ln(1, s"virtual ~Handler();")

    def mkDefaultHandlers() {
      ln(1, s"virtual void handleDiscard(mgen::MGenBase& o);")
      ln(1, s"virtual void handleUnknown(mgen::MGenBase& o);")
    }

    def mkHandler(t: ClassType) {
      ln(1, s"virtual void handle(${MkLongTypeName.cpp(t)}& o);")
    }

    def mkHandlers() {
      allClasses foreach mkHandler
    }

    mkDefaultHandlers()
    mkHandlers()

    endl()

  }

}