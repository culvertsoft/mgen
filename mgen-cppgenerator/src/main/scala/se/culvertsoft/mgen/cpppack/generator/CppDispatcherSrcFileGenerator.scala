package se.culvertsoft.mgen.cpppack.generator

import java.io.File
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object CppDispatchSrcFileGenerator extends CppDispatchGenerator(SrcFile) {

  override def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include(CppDispatchGenerator.includeStringH(param.nameSpaceString))
    endl()
  }

  override def mkDispatch(param: UtilClassGenParam) {
    ln(s"void dispatch(mgen::MGenBase& object, ${param.nameSpaceString}::Handler& handler) {")
    ln(s"}").endl()
  }
}