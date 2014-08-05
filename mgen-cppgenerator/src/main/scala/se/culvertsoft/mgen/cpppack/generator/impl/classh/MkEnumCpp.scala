package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.EnumEntry
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils

object MkEnumCpp {

  def apply(module: Module, _e: EnumType, generatorSettings: java.util.Map[String, String]): String = {

    implicit val txtBuffer = SuperStringBuffer.getCached()
    implicit val currentModule = module
    val namespaces = currentModule.path().split("\\.")

    val name = _e.shortName()
    val fullname = _e.fullName().replaceAllLiterally(".", "::")
    val entries = _e.entries() ++ List(new EnumEntry("UNKNOWN", null))
    val ns = namespaces.mkString("::")

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

    ln(s"static std::vector<$name> _mk_${name}_enum_values() {")
    ln(1, s"std::vector<$name> out;")
    for (e <- entries) {
      ln(1, s"out.push_back(${name}_${e.name});")
    }
    ln(1, s"return out;")
    ln(s"}")
    ln()

    ln(s"static std::vector<std::string> _mk_${name}_enum_names() {")
    ln(1, s"std::vector<std::string> out;")
    for (e <- entries) {
      ln(1, s"out.push_back(${quote(e.name)});")
    }
    ln(1, s"return out;")
    ln(s"}")
    ln()

    CppGenUtils.mkNameSpacesEnd(namespaces)

    CppGenUtils.mkNameSpaces(List("mgen"))

    ln(s"const std::vector<$fullname>& get_enum_values(const $fullname /* type_evidence */) {")
    ln(1, s"static const std::vector<$fullname> out = ${ns}::_mk_${name}_enum_values();")
    ln(1, s"return out;")
    ln("}")
    ln()

    ln(s"$fullname get_enum_value(const $fullname /* type_evidence */, const std::string& enumName) {")
    ln(1, s"static const std::map<std::string, $fullname> lkup = ${ns}::_mk_${name}_enum_lkup_map();")
    ln(1, s"std::map<std::string, $fullname>::const_iterator it = lkup.find(enumName);")
    ln(1, s"return it != lkup.end() ? it->second : $ns::${name}_UNKNOWN;")
    ln(s"}")
    ln()

    ln(s"const std::vector<std::string>& get_enum_names(const $fullname /* type_evidence */) {")
    ln(1, s"static const std::vector<std::string> out = ${ns}::_mk_${name}_enum_names();")
    ln(1, s"return out;")
    ln("}")
    ln()

    ln(s"const std::string& get_enum_name(const $fullname enumValue) {");
    for (e <- entries)
      ln(1, s"const static std::string ${e.name}_name(${quote(e.name)});")

    ln(1, s"switch (enumValue) {")
    for (e <- entries) {
      ln(2, s"case $ns::${name}_${e.name}:")
      ln(3, s"return ${e.name}_name;")
    }
    ln(2, s"default:")
    ln(3, s"return UNKNOWN_name;")
    ln(1, "}")
    ln(s"}")
    ln()

    CppGenUtils.mkNameSpacesEnd(List("mgen"))

    txtBuffer.toString()

  }

}