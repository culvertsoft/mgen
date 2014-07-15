package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldIdString
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldMetaString

object MkFieldById {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.fieldsInclSuper()

    val pfx = s"_${t.shortName}"
    
    ln(0, s"const mgen::Field * ${t.shortName()}::_fieldById(const short id) const {")
    ln(1, s"switch (id) {")
    for (field <- allFields) {
      ln(1, s"case ${fieldIdString(field)}:")
      ln(2, s"return &${fieldMetaString(field)};")
    }
    ln(1, s"default:")
    ln(2, s"return 0;");
    ln(1, s"}")
    ln(0, s"}")
    endl()

    ln(0, s"const mgen::Field * ${t.shortName()}::_fieldByName(const std::string& name) const {")
    ln(1, s"static const std::map<std::string, const mgen::Field*> name2meta = ${pfx}_field_names2metadata_make();")
    ln(1, s"const std::map<std::string, const mgen::Field*>::const_iterator it = name2meta.find(name);")
    ln(1, s"return it != name2meta.end() ? it->second : 0;")
    ln(0, s"}")
    endl()

  }

}