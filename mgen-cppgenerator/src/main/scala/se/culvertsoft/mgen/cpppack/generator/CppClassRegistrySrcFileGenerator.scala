package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkGetByTypeIds16Bit
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkGetByTypeIds16BitBase64

object CppClassRegistrySrcFileGenerator extends CppClassRegistryGenerator(SrcFile) {

  override def mkIncludes(param: UtilClassGenParam) {

    CppGenUtils.include("ClassRegistry.h")

    val mkUnityBuild = param.settings.getOrElse("generate_unity_build", throw new GenerationException("Missing <generate_unity_build> setting for C++ generator")).toBoolean

    if (mkUnityBuild) {
      val ts = param.modules.flatMap(_.types)
      for (t <- ts)
        CppGenUtils.include(t, ".cpp")
      CppGenUtils.include(CppDispatchGenerator.includeStringCpp(param.nameSpaceString))
      CppGenUtils.include(CppHandlerGenerator.includeStringCpp(param.nameSpaceString))
    }

    endl()

  }

  override def mkDefaultCtor(param: UtilClassGenParam) {
    ln(s"ClassRegistry::ClassRegistry() {")
    for (m <- param.modules)
      for (t <- m.types)
        ln(1, s"add<${t.fullName.replaceAllLiterally(".", "::")}>();")
    ln(s"}").endl()
  }

  override def mkDestructor(param: UtilClassGenParam) {
    ln("ClassRegistry::~ClassRegistry() {")
    ln("}").endl()
  }

  override def mkGetByTypeIds16Bit(param: UtilClassGenParam) {
    MkGetByTypeIds16Bit(0, param.nameSpaceString, param.modules, param.settings)
  }

  override def mkGetByTypeIds16BitBase64(param: UtilClassGenParam) {
    MkGetByTypeIds16BitBase64(0, param.nameSpaceString, param.modules, param.settings)
  }

}