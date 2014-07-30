package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln

object CppDispatchHeaderGenerator extends CppDispatchGenerator(Header) {

  override def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include("Handler.h")
    endl()
  }

  override def mkDispatch(param: UtilClassGenParam) {
    ln(s"void dispatch(mgen::MGenBase& object, ${param.nameSpaceString}::Handler& handler);")
    endl()
  }
}