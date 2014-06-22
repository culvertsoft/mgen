package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import se.culvertsoft.mgen.api.model.CustomType

object MkLongTypeName {
  def underscore(t: CustomType): String = {
    t.fullName().replaceAllLiterally(".", "_")
  }
  def cpp(t: CustomType): String = {
    t.fullName().replaceAllLiterally(".", "::")
  }
  def staticClassRegEntry(t: CustomType): String = {
    val underscoreName = underscore(t)
    val cppName = cpp(t)
    val typeIds = s"$cppName::_type_ids()"
    val typeName = s"$cppName::_type_name()"
    val ctor = s"$cppName::_newInstance"
    s"static const mgen::ClassRegistryEntry $underscoreName($typeIds, $typeName, $ctor);"
  }
}