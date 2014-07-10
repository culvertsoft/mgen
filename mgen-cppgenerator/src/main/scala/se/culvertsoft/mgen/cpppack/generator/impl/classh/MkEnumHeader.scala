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

object MkEnumHeader {

  def apply(module: Module, _e: EnumType, generatorSettings: java.util.Map[String, String]): String = {

    implicit val txtBuffer = new SuperStringBuffer
    implicit val currentModule = module
    val namespaces = currentModule.path().split("\\.")
    val fullname = _e.fullName().replaceAllLiterally(".", "::")

    val name = _e.shortName()
    val entries = _e.entries() ++ List(new EnumEntryImpl("UNKNOWN", null))

    txtBuffer.clear()

    CppGenUtils.mkFancyHeader()
    CppGenUtils.mkIncludeGuardStart(_e.fullName())

    CppGenUtils.includeT("string")
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
    
    
    ln(s"$fullname get_enum_value(const $fullname /* type_evidence */, const std::string& enumName);")
    ln()

    ln(s"const std::string& get_enum_name(const $fullname enumValue);");
    ln()
    
    CppGenUtils.mkNameSpacesEnd(List("mgen"))
    CppGenUtils.mkIncludeGuardEnd()
    txtBuffer.toString()

  }

}