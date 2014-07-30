package se.culvertsoft.mgen.cpppack.generator

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkLongTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.utilh.MkTypeIdSwitch

object CppDispatchSrcFileGenerator extends CppDispatchGenerator(SrcFile) {

  override def mkIncludes(param: UtilClassGenParam) {
    CppGenUtils.include("Dispatcher.h")
    CppGenUtils.include("ClassRegistry.h")
    endl()
  }

  override def mkDispatch(param: UtilClassGenParam) {

    val allTypes = param.modules.flatMap(_.types())
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    ln(s"void dispatch(mgen::MGenBase& object, ${param.nameSpaceString}::Handler& handler) {")

    ln(1, "const std::vector<short>& ids = object._typeIds16Bit();")
    ln(1, "std::size_t i = 0;")
    MkTypeIdSwitch.apply(
      s => s,
      true,
      1,
      "handler.handleUnknown(object);",
      topLevelTypes,
      t => s"${MkLongTypeName.cpp(t)}::_type_id_16bit",
      t => s"handler.handle(static_cast<${MkLongTypeName.cpp(t)}&>(object));")

    ln("}").endl()

  }
}