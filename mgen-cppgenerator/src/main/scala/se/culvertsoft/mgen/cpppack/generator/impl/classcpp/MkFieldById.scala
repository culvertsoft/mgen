package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.quote
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldIdString
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldMetaString

object MkFieldById {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    val allFields = t.fieldsInclSuper()

    val pfx = s"_${t.shortName}"

    ln(s"const mgen::Field * ${t.shortName()}::_fieldById(const short id) const {")
    if (allFields.nonEmpty) {
      ln(1, s"switch (id) {")
      for (field <- allFields) {
        ln(1, s"case ${fieldIdString(field)}:")
        ln(2, s"return &${fieldMetaString(field)};")
      }
      ln(1, s"default:")
      ln(2, s"return 0;");
      ln(1, s"}")
    } else {
      ln(1, s"return 0;")
    }
    ln(s"}")
    endl()

    val lkup = t.fieldsInclSuper.map(f => s"(${quote(f.name)}, &${t.shortName}::${fieldMetaString(f)})")
    val lkupString =
      if (lkup.isEmpty) ";"
      else s" = mgen::make_map<std::string, const mgen::Field*>()${lkup.mkString("")};"

    ln(0, s"const mgen::Field * ${t.shortName()}::_fieldByName(const std::string& name) const {")
    //ln(1, s"static const std::map<std::string, const mgen::Field*> name2meta = ${pfx}_field_names2metadata_make();")
    ln(1, s"static const std::map<std::string, const mgen::Field*> name2meta$lkupString")
    ln(1, s"const std::map<std::string, const mgen::Field*>::const_iterator it = name2meta.find(name);")
    ln(1, s"return it != name2meta.end() ? it->second : 0;")
    ln(0, s"}")
    endl()

  }

}