package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkGetByTypeIds16BitBase64 {

  def apply(
    nTabs: Int,
    namespaceString: String,
    referencedModules: Seq[Module],
    generatorSettings: Map[String, String])(implicit txtBuffer: SuperStringBuffer) {

    val allTypes = referencedModules.flatMap(_.types()).map(_._2).distinct

    ln(nTabs, s"const mgen::ClassRegistryEntry * $namespaceString::ClassRegistry::getByIds(const std::vector<std::string>& ids) const {")

    for (t <- allTypes)
      ln(nTabs + 1, MkLongTypeName.staticClassRegEntry(t))

    txtBuffer.endl()

    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    ln(nTabs + 1, "int i = 0;")
    MkTypeIdSwitch.apply(
      s => s"getTypeId16bitFromTypeId16BitBase64($s)",
      true,
      nTabs + 1,
      "return 0;",
      topLevelTypes,
      t => s"${MkLongTypeName.cpp(t)}::_type_id_16bit",
      t => s"return &${MkLongTypeName.underscore(t)};")

    ln(nTabs + 1, "return 0;")

    ln(nTabs, "}").endl()

  }

}