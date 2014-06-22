package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkReadObjectFieldsDispatch
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkVisitorDispatch
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkGetByTypeIds16Bit
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkGetByTypeIds16BitBase64
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object CppClassRegistryHeaderGenerator extends CppClassRegistryGenerator(".h") {

  override def mkIncludes(
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]) {

    ln("#include \"mgen/classes/ClassRegistryBase.h\"")

    for (referencedModule <- referencedModules)
      for (t <- referencedModule.types().values())
        CppGenUtils.include(t)

    endl()

  }

  override def mkClassStart(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    CppGenUtils.mkClassStart("ClassRegistry", "mgen::ClassRegistryBase")
    ln("public:").endl()
  }

  override def mkDefaultCtor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    ln(1, s"ClassRegistry();")
  }

  override def mkDestructor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    ln(1, s"virtual ~ClassRegistry();")
    endl()
  }

  def quote(s: String): String = {
    '"' + s + '"'
  }

  override def mkReadObjectFields(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    MkReadObjectFieldsDispatch(referencedModules, namespacesstring, generatorSettings)
  }

  override def mkVisitObjectFields(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    MkVisitorDispatch(referencedModules, namespacesstring, generatorSettings)
  }

  override def mkGetByTypeIds16Bit(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    ln(1, s"const mgen::ClassRegistryEntry * getByTypeIds16Bit(const std::vector<short>& ids) const;").endl()
  }

  override def mkGetByTypeIds16BitBase64(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    ln(1, s"const mgen::ClassRegistryEntry * getByTypeIds16BitBase64(const std::vector<std::string>& ids) const;").endl()
  }

  override def mkClassEnd(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    CppGenUtils.mkClassEnd("ClassRegistry")
  }

}