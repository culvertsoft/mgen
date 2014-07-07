package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.compiler.internal.BuiltInStaticLangGenerator._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType

object MkReadObjectFieldsDispatch {

  def fullName(t: CustomType): String = {
    MkLongTypeName.cpp(t)
  }

  def apply(
    referencedModules: Seq[Module],
    generatorSettings: Map[String, String])(implicit txtBuffer: SuperStringBuffer) {

    val nTabs = 1
    val allTypes = referencedModules.flatMap(_.types()).map(_._2).distinct
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    txtBuffer.tabs(1).textln(s"template<typename ContextType, typename ReaderType>")
    txtBuffer.tabs(1).textln(s"void readObjectFields(mgen::MGenBase& o, ContextType& context, ReaderType& reader) const {")

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
      t => s"reader.readFields(static_cast<${fullName(t)}&>(o), context);")

    ln(nTabs + 1, "return;")
    /*
   txtBuffer.tabs(2).textln("default: // should not happen...INCORRECT USAGE!")
    txtBuffer.tabs(3).textln("throw mgen::Exception(")
    txtBuffer.tabs(5).textln("std::string(")
    txtBuffer.tabs(7).textln(s"${quote(s"${namespaceString}::ClassRegistry::readObjectFields: Incorrect usage. Class '")}).append(")
    txtBuffer.tabs(7).textln("o._typeName()).append(\" not registered.\"));")
    txtBuffer.tabs(2).textln(s"}")
*/
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()
  }

}