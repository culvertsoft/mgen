package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

import scala.collection.JavaConversions._

object CppHandlerHeaderGenerator extends CppHandlerGenerator(Header) {

  override def mkIncludes(param: UtilClassGenParam) {
    super.mkIncludes(param)
    CppGenUtils.include(ForwardDeclareGenerator.includeStringH(param.nameSpaceString))
    endl()

  }
}