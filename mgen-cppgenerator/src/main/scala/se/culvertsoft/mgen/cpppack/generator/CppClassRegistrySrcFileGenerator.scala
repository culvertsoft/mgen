package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable
import scala.collection.JavaConverters.mapAsScalaMapConverter
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkGetByTypeIds16Bit
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkGetByTypeIds16BitBase64
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object CppClassRegistrySrcFileGenerator extends CppClassRegistryGenerator(".cpp") {

  override def mkIncludes(
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]) {

    CppGenUtils.include("ClassRegistry.h")

    val mkUnityBuild = generatorSettings.asScala.getOrElse("generate_unity_build", throw new GenerationException("Missing <generate_unity_build> setting for C++ generator")).toBoolean

    if (mkUnityBuild) {
      for (referencedModule <- referencedModules)
        for (t <- referencedModule.types().values())
          CppGenUtils.include(t, ".cpp")
    }

    endl()

  }

  override def mkDefaultCtor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    ln(s"ClassRegistry::ClassRegistry() {")
    for (m <- referencedModules)
      for (t <- m.types().values())
        ln(1, s"add<${t.fullName().replaceAllLiterally(".", "::")}>();")
    ln(s"}").endl()
  }

  override def mkDestructor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    ln("ClassRegistry::~ClassRegistry() {")
    ln("}").endl()
  }

  override def mkGetByTypeIds16Bit(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    MkGetByTypeIds16Bit(0, referencedModules, namespacesstring, generatorSettings)
  }

  override def mkGetByTypeIds16BitBase64(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    MkGetByTypeIds16BitBase64(0, referencedModules, namespacesstring, generatorSettings)
  }

}