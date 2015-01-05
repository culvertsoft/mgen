package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.EnumEntry
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.txt
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenUtils

object MkEnumHeader {

  def apply(_e: EnumType): String = {

    implicit val txtBuffer = SourceCodeBuffer.getThreadLocal()
    implicit val module = _e.module
    val namespaces = module.path().split("\\.")
    val fullname = _e.fullName().replaceAllLiterally(".", "::")

    val name = _e.shortName()
    val entries = _e.entries() ++ List(new EnumEntry("UNKNOWN", null))

    txtBuffer.clear()

    CppGenUtils.mkFancyHeader()
    CppGenUtils.mkIncludeGuardStart(_e.fullName())

    CppGenUtils.include("mgen/classes/MGenBase.h")
    endl()

    CppGenUtils.mkNameSpaces(namespaces)

    ln(s"enum ${name} {")
    val values = new ArrayBuffer[Int]

    var curVal = -1

    for (entry <- entries) {
      curVal =
        if (entry.constant() != null)
          java.lang.Integer.decode(entry.constant())
        else
          curVal + 1;
      values += curVal
      txt(1, s"${name}_${entry.name} = $curVal")
      if (entry != entries.last)
        ln(",")
    }

    ln()
    ln("};")
    ln()

    CppGenUtils.mkNameSpacesEnd(namespaces)

    CppGenUtils.mkNameSpaces(List("mgen"))

    ln(s"const std::vector<$fullname>& get_enum_values(const $fullname /* type_evidence */);")
    ln(s"$fullname get_enum_value(const $fullname /* type_evidence */, const std::string& enumName);")
    ln()

    ln(s"const std::vector<std::string>& get_enum_names(const $fullname /* type_evidence */);")
    ln(s"const std::string& get_enum_name(const $fullname enumValue);");
    ln()
    /*
    ln(s"inline Type::TAG TAG_OF(const $fullname * /* type_evidence */) { return Type::TAG_STRING; }")
    ln()*/

    CppGenUtils.mkNameSpacesEnd(List("mgen"))
    CppGenUtils.mkIncludeGuardEnd()
    txtBuffer.toString()

  }

}