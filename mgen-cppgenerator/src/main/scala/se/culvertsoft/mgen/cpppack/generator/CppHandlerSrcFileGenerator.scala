package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object CppHandlerSrcFileGenerator extends CppHandlerGenerator(SrcFile) {
  
  override def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include(CppHandlerGenerator.includeStringH(param.nameSpaceString))
    endl()
  }
  
}