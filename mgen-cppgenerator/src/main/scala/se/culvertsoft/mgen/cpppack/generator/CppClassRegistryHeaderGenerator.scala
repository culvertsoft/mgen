package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.collectionAsScalaIterable
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkReadObjectFieldsDispatch
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkVisitorDispatch

object CppClassRegistryHeaderGenerator extends CppClassRegistryGenerator(".h") {

  override def mkIncludes(
    referencedModules: Seq[Module],
    generatorSettings: java.util.Map[String, String]) {

    txtBuffer.textln("#include \"mgen/classes/ClassRegistryBase.h\"")

    for (referencedModule <- referencedModules)
      for (t <- referencedModule.types().values())
        CppGenUtils.include(t)

    txtBuffer.endl()

  }

  override def mkClassStart(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    CppGenUtils.mkClassStart("ClassRegistry", "mgen::ClassRegistryBase")
    txtBuffer.textln("public:")
    txtBuffer.endl()
  }

  override def mkDefaultCtor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    txtBuffer.tabs(1).textln(s"ClassRegistry();")
  }

  override def mkDestructor(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    txtBuffer.tabs(1).textln(s"virtual ~ClassRegistry();")
    txtBuffer.endl()
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

  override def mkClassEnd(referencedModules: Seq[Module], generatorSettings: java.util.Map[String, String]) {
    CppGenUtils.mkClassEnd("ClassRegistry")
  }

}