package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkReadObjectFieldsDispatch {

  def fullName(t: ClassType): String = {
    MkLongTypeName.cpp(t)
  }

  def apply(
    referencedModules: Seq[Module],
    generatorSettings: Map[String, String])(implicit txtBuffer: SuperStringBuffer) {

    val nTabs = 1
    val allClasses = referencedModules.flatMap(_.classes)
    val toplevelClasses = allClasses.filterNot(_.hasSuperType)

    txtBuffer.tabs(1).textln(s"template<typename ContextType, typename ReaderType>")
    txtBuffer.tabs(1).textln(s"void readObjectFields(mgen::MGenBase& o, ContextType& context, ReaderType& reader) const {")

    txtBuffer.endl()

    ln(nTabs + 1, "const std::vector<short>& ids = o._typeIds16Bit();").endl()

    ln(nTabs + 1, "std::size_t i = 0;")
    MkTypeIdSwitch.apply(
      s => s,
      true,
      nTabs + 1,
      "return;",
      toplevelClasses,
      t => s"${MkLongTypeName.cpp(t)}::_type_id_16bit",
      t => s"reader.readFields(static_cast<${fullName(t)}&>(o), context);")

    ln(nTabs + 1, "return;")

    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()
  }

}