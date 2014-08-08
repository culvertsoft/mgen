package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkAllMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkConstants
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDeepCopy
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDefaultCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDestructor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkEqOperator
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkEquals
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkFieldById
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkHasers
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkIncludes
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkIsFieldSet
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkMetadataFields
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkMetadataGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkNewInstance
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkNumFieldsSet
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkRequiredMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkSetFieldsSet
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkSetters
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkValidate

object CppSrcFile extends CppSrcFileOrHeader(".cpp") {

  override def mkIncludes(t: ClassType) {
    MkIncludes(t, currentModule)
  }

  override def mkConstants(t: ClassType) {
    MkConstants(t, currentModule)
  }

  override def mkDefaultCtor(t: ClassType) {
    MkDefaultCtor(t, currentModule)
  }

  override def mkRequiredMembersCtor(t: ClassType) {
    MkRequiredMembersCtor(t, currentModule)
  }

  override def mkAllMembersCtor(t: ClassType) {
    MkAllMembersCtor(t, currentModule)
  }

  override def mkFieldById(t: ClassType) {
    MkFieldById(t, currentModule)
  }

  override def mkEqOperator(t: ClassType) {
    MkEqOperator(t, currentModule)
  }

  override def mkEquals(t: ClassType) {
    MkEquals(t, currentModule)
  }

  override def mkDeepCopy(t: ClassType) {
    MkDeepCopy(t, currentModule)
  }

  override def mkDestructor(t: ClassType) {
    MkDestructor(t, currentModule)
  }

  override def mkGetters(t: ClassType) {
    MkGetters(t, currentModule)
  }

  override def mkSetters(t: ClassType) {
    MkSetters(t, currentModule)
  }

  override def mkHasers(t: ClassType) {
    MkHasers(t, currentModule)
  }

  override def mkNewInstance(t: ClassType) {
    MkNewInstance(t, currentModule)
  }

  override def mkMetadataGetters(t: ClassType) {
    MkMetadataGetters(t, currentModule)
  }

  override def mkMetaDataFields(t: ClassType) {
    MkMetadataFields(t, currentModule)
  }

  override def mkSetFieldsSet(t: ClassType) {
    MkSetFieldsSet(t, currentModule)
  }

  override def mkNumFieldsSet(t: ClassType) {
    MkNumFieldsSet(t, currentModule)
  }

  override def mkValidate(t: ClassType) {
    MkValidate(t, currentModule)
  }

  override def mkIsFieldSet(t: ClassType) {
    MkIsFieldSet(t, currentModule)
  }

}