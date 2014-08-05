package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import se.culvertsoft.mgen.api.model.ClassType

object MkLongTypeName {
  def underscore(t: ClassType): String = {
    t.fullName().replaceAllLiterally(".", "_")
  }
  def cpp(t: ClassType): String = {
    t.fullName().replaceAllLiterally(".", "::")
  }
  def staticClassRegEntry(t: ClassType): String = {
    val underscoreName = underscore(t)
    val cppName = cpp(t)
    val typeIds = s"$cppName::_type_ids()"
    val typeName = s"$cppName::_type_name()"
    val ctor = s"$cppName::_newInstance"
    s"static const mgen::ClassRegistryEntry $underscoreName($typeIds, $typeName, $ctor);"
  }
}