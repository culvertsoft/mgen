package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType

object MkVisitorDispatch {

  def fullName(t: CustomType): String = {
    MkLongTypeName.cpp(t)
  }
  
  def apply(
    referencedModules: Seq[Module],
    generatorSettings: Map[String, String])(implicit txtBuffer: SuperStringBuffer) {

    val nTabs = 1
    val allTypes = referencedModules.flatMap(_.types)
    val topLevelTypes = allTypes.filterNot(_.hasSuperType)

    for (constString <- List("", "const ")) {

      ln(1, s"template<typename VisitorType>")
      ln(1, s"void visitObject(${constString}mgen::MGenBase& o, VisitorType& visitor) const {")

      txtBuffer.endl()

      ln(nTabs + 1, "const std::vector<short>& ids = o._typeIds16Bit();").endl()

      ln(nTabs + 1, "int i = 0;")
      MkTypeIdSwitch.apply(
        s => s,
        true,
        nTabs + 1,
        "return;",
        topLevelTypes,
        t => s"${MkLongTypeName.cpp(t)}::_type_id_16bit",
        t => s"static_cast<${constString}${fullName(t)}&>(o)._accept<VisitorType>(visitor);")

      ln(nTabs + 1, "return;")

      ln(1, "}").endl()

    }

  }
}