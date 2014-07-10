package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.impl.EnumEntryImpl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils

object MkEnumCpp {

  def apply(module: Module, _e: EnumType, generatorSettings: java.util.Map[String, String]): String = {

    implicit val txtBuffer = new SuperStringBuffer
    implicit val currentModule = module
    val namespaces = currentModule.path().split("\\.")

    val name = _e.shortName()
    val entries = _e.entries() ++ List(new EnumEntryImpl("UNKNOWN", null))

    txtBuffer.clear()

    CppGenUtils.mkFancyHeader()
    CppGenUtils.include(s"$name.h")
    CppGenUtils.includeT("map")
    ln()

    CppGenUtils.mkNameSpaces(namespaces)

    val values = new ArrayBuffer[Int]

    var curVal = -1
    for (entry <- entries) {
      curVal =
        if (entry.constant() != null)
          java.lang.Integer.decode(entry.constant())
        else
          curVal + 1;
      values += curVal
    }

    ln(s"static std::map<std::string, $name> _mk_${name}_enum_lkup_map() {")
    ln(1, s"std::map<std::string, $name> out;")
    for (e <- entries) {
      ln(1, s"out[${quote(e.name)}] = ${name}_${e.name};")
    }
    ln(1, s"return out;")
    ln(s"}")
    ln()

    ln(s"$name _get_enum_value(const $name /* type_evidence */, const std::string& enumName) {")
    ln(1, s"static const std::map<std::string, $name> lkup = _mk_${name}_enum_lkup_map();")
    ln(1, s"std::map<std::string, $name>::const_iterator it = lkup.find(enumName);")
    ln(1, s"return it != lkup.end() ? it->second : ${name}_UNKNOWN;")
    ln(s"}")
    ln()

    ln(s"const std::string& _get_enum_name(const $name enumValue) {");
    for (e <- entries)
      ln(1, s"const static std::string ${e.name}_name(${quote(e.name)});")

    ln(1, s"switch (value) {")
    for (e <- entries) {
      ln(2, s"case ${name}_${e.name}:")
      ln(3, s"return ${e.name}_name;")
    }
    ln(2, s"default:")
    ln(3, s"return UNKNOWN_name;")
    ln(1, "}")
    ln(s"}")
    ln()

    CppGenUtils.mkNameSpacesEnd(namespaces)
    txtBuffer.toString()

  }

}