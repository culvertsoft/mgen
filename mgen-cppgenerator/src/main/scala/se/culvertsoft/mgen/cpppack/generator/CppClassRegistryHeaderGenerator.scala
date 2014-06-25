package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkReadObjectFieldsDispatch
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkVisitorDispatch

object CppClassRegistryHeaderGenerator extends CppClassRegistryGenerator(Header) {

  override def mkIncludes(param: UtilClassGenParam) {

    ln("#include \"mgen/classes/ClassRegistryBase.h\"")

    for (referencedModule <- param.modules)
      for (t <- referencedModule.types().values())
        CppGenUtils.include(t)

    endl()

  }

  override def mkClassStart(param: UtilClassGenParam) {
    super.mkClassStart(param)
    ln("public:").endl()
  }

  override def mkDefaultCtor(param: UtilClassGenParam) {
    ln(1, s"ClassRegistry();")
  }

  override def mkDestructor(param: UtilClassGenParam) {
    ln(1, s"virtual ~ClassRegistry();")
    endl()
  }

  override def mkReadObjectFields(param: UtilClassGenParam) {
    MkReadObjectFieldsDispatch(param.modules, param.settings)
  }

  override def mkVisitObjectFields(param: UtilClassGenParam) {
    MkVisitorDispatch(param.modules, param.settings)
  }

  override def mkGetByTypeIds16Bit(param: UtilClassGenParam) {
    ln(1, s"const mgen::ClassRegistryEntry * getByIds(const std::vector<short>& ids) const;").endl()
  }

  override def mkGetByTypeIds16BitBase64(param: UtilClassGenParam) {
    ln(1, s"const mgen::ClassRegistryEntry * getByIds(const std::vector<std::string>& ids) const;").endl()
  }

}