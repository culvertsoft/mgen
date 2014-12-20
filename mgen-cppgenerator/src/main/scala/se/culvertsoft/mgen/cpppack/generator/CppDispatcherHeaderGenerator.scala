package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object CppDispatchHeaderGenerator extends CppDispatchGenerator(Header) {

  override def mkIncludes(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.include("Handler.h")
    endl()
  }

  override def mkDispatch(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    ln(s"void dispatch(mgen::MGenBase& object, ${param.nameSpaceString}::Handler& handler);")
    endl()
  }
}