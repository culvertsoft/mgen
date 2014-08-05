package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkGetByTypeIds16Bit {

  def apply(
    nTabs: Int,
    namespaceString: String,
    referencedModules: Seq[Module],
    generatorSettings: Map[String, String])(implicit txtBuffer: SuperStringBuffer) {

    val allClasses = referencedModules.flatMap(_.classes)

    ln(nTabs, s"const mgen::ClassRegistryEntry * $namespaceString::ClassRegistry::getByIds(const std::vector<short>& ids) const {")

    for (t <- allClasses)
      ln(nTabs + 1, MkLongTypeName.staticClassRegEntry(t))

    txtBuffer.endl()

    val topLevelClasses = allClasses.filterNot(_.hasSuperType)

    ln(nTabs + 1, "std::size_t i = 0;")
    MkTypeIdSwitch.apply(
      s => s,
      true,
      nTabs + 1,
      "return 0;",
      topLevelClasses,
      t => s"${MkLongTypeName.cpp(t)}::_type_id_16bit",
      t => s"return &${MkLongTypeName.underscore(t)};")

    ln(nTabs + 1, "return 0;")

    ln(nTabs, "}").endl()

  }

}