package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkReadObjectFieldsDispatch
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkVisitorDispatch

object CppClassRegistryHeaderGenerator extends CppClassRegistryGenerator(Header) {

  override def mkIncludes(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {

    ln("#include \"mgen/classes/ClassRegistryBase.h\"")

    for (referencedModule <- param.modules)
      for (t <- referencedModule.classes)
        CppGenUtils.include(t)

    endl()

  }

  override def mkClassStart(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    super.mkClassStart(param)
    ln("public:").endl()
  }

  override def mkDefaultCtor(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    ln(1, s"ClassRegistry();")
  }

  override def mkDestructor(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    ln(1, s"virtual ~ClassRegistry();")
    endl()
  }

  override def mkReadObjectFields(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    MkReadObjectFieldsDispatch(param.modules, param.settings)
  }

  override def mkVisitObjectFields(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    MkVisitorDispatch(param.modules, param.settings)
  }

  override def mkGetByTypeIds16Bit(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    ln(1, s"const mgen::ClassRegistryEntry * getByIds(const std::vector<short>& ids) const;").endl()
  }

  override def mkGetByTypeIds16BitBase64(param: UtilClassGenParam)(implicit txtBuffer: SourceCodeBuffer) {
    ln(1, s"const mgen::ClassRegistryEntry * getByIds(const std::vector<std::string>& base64ids_vector) const {")
    ln(2, s"return mgen::ClassRegistryBase::getByIds(base64ids_vector);")
    ln(1, s"}")
    endl()
    ln(1, s"const mgen::ClassRegistryEntry * getByIds(const std::string& base64ids_concatenated) const {")
    ln(2, s"return mgen::ClassRegistryBase::getByIds(base64ids_concatenated);")
    ln(1, s"}")
    endl()
  }

}