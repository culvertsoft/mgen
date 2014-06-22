package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkAllMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDeepCopy
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDefaultCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkDestructor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkEquals
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkFieldById
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkIncludes
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkIsFieldSet
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkMetadataFieldMakers
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkMetadataFields
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkMetadataGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkNewInstance
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkNumFieldsSet
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkRequiredMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkSetFieldsSet
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkSetters
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkValidate
import se.culvertsoft.mgen.cpppack.generator.impl.classcpp.MkEqOperator

object CppSrcFile extends CppSrcFileOrHeader(".cpp") {

  override def mkIncludes(t: CustomType) {
    MkIncludes(t, currentModule)
  }

  override def mkDefaultCtor(t: CustomType) {
    MkDefaultCtor(t, currentModule)
  }

  override def mkRequiredMembersCtor(t: CustomType) {
    MkRequiredMembersCtor(t, currentModule)
  }

  override def mkAllMembersCtor(t: CustomType) {
    MkAllMembersCtor(t, currentModule)
  }

  override def mkFieldById(t: CustomType) {
    MkFieldById(t, currentModule)
  }

  override def mkEqOperator(t: CustomType) {
    MkEqOperator(t, currentModule)
  }

  override def mkEquals(t: CustomType) {
    MkEquals(t, currentModule)
  }

  override def mkDeepCopy(t: CustomType) {
    MkDeepCopy(t, currentModule)
  }

  override def mkDestructor(t: CustomType) {
    MkDestructor(t, currentModule)
  }

  override def mkGetters(t: CustomType) {
    MkGetters(t, currentModule)
  }

  override def mkSetters(t: CustomType) {
    MkSetters(t, currentModule)
  }

  override def mkNewInstance(t: CustomType) {
    MkNewInstance(t, currentModule)
  }

  override def mkMetadataGetters(t: CustomType) {
    MkMetadataGetters(t, currentModule)
  }

  override def mkMetaDataFields(t: CustomType) {
    MkMetadataFields(t, currentModule)
  }

  override def mkMetaDataFieldMakers(t: CustomType) {
    MkMetadataFieldMakers(t, currentModule)
  }

  override def mkSetFieldsSet(t: CustomType) {
    MkSetFieldsSet(t, currentModule)
  }

  override def mkNumFieldsSet(t: CustomType) {
    MkNumFieldsSet(t, currentModule)
  }

  override def mkValidate(t: CustomType) {
    MkValidate(t, currentModule)
  }

  override def mkIsFieldSet(t: CustomType) {
    MkIsFieldSet(t, currentModule)
  }

}