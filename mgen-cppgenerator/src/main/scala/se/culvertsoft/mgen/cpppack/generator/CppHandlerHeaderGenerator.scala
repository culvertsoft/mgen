package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.api.model.CustomType
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

    val allTypes = param.modules.flatMap(_.types).map(_._2).distinct
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    ln(1, s"Handler();")
    ln(1, s"virtual ~Handler();")

    def mkDefaultHandlers() {
      ln(1, s"virtual void handleDiscard(mgen::MGenBase& o);")
      ln(1, s"virtual void handleUnknown(mgen::MGenBase& o);")
    }

    def mkHandler(t: CustomType) {
      ln(1, s"virtual void handle(${MkLongTypeName.cpp(t)}& o);")
    }

    def mkHandlers() {
      allTypes foreach mkHandler
    }

    mkDefaultHandlers()
    mkHandlers()

    endl()

  }

}