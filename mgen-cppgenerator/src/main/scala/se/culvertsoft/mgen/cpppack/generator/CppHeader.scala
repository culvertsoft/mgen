package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkAcceptVisitor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkAllMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkDeepCopy
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkDefaultCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkDestructor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkEqOperator
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkEquals
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkFieldById
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkIncludes
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkIsFieldSet
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkMembers
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkMetadataFieldMakers
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkMetadataFields
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkMetadataGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkNewInstance
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkNumFieldsSet
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkReadField
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkRequiredMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkSetFieldsSet
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkSetters
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkUsingStatements
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkValidate
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkHasers

object CppHeader extends CppSrcFileOrHeader(".h") {

  override def mkIncludes(t: CustomType) {
    MkIncludes(t, currentModule)
  }

  override def mkIncludeGuardStart(module: Module, t: CustomType) {
    CppGenUtils.mkIncludeGuardStart(t.fullName())
  }

  override def mkIncludeGuardEnd() {
    CppGenUtils.mkIncludeGuardEnd()
  }

  override def mkEqOperator(t: CustomType) {
    MkEqOperator(t, currentModule)
  }

  override def mkClassStart(t: CustomType) {
    CppGenUtils.mkClassStart(t.shortName(), getSuperTypeNameString(t))
  }

  override def mkPrivate() {
    txtBuffer.textln("private:")
  }

  override def mkPublic() {
    txtBuffer.textln("public:")
  }

  override def mkDefaultCtor(t: CustomType) {
    MkDefaultCtor(t, currentModule)
  }

  override def mkAllMembersCtor(t: CustomType) {
    MkAllMembersCtor(t, currentModule)
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

  override def mkHasers(t: CustomType) {
    MkHasers(t, currentModule)
  }
  
  override def mkMembers(t: CustomType) {
    MkMembers(t, currentModule)
  }

  override def mkEquals(t: CustomType) {
    MkEquals(t, currentModule)
  }

  override def mkMetaDataFields(t: CustomType) {
    MkMetadataFields(t, currentModule)
  }

  override def mkReadField(t: CustomType) {
    MkReadField(t, currentModule)
  }

  override def mkAcceptVisitor(t: CustomType) {
    MkAcceptVisitor(t, currentModule)
  }

  override def mkFieldById(t: CustomType) {
    MkFieldById(t, currentModule)
  }

  override def mkMetadataGetters(t: CustomType) {
    MkMetadataGetters(t, currentModule)
  }

  override def mkNewInstance(t: CustomType) {
    MkNewInstance(t, currentModule)
  }

  override def mkDeepCopy(t: CustomType) {
    MkDeepCopy(t, currentModule)
  }

  override def mkUsingStatements(t: CustomType) {
    MkUsingStatements(t, currentModule)
  }

  override def mkMetaDataFieldMakers(t: CustomType) {
    MkMetadataFieldMakers(t, currentModule)
  }

  override def mkSetFieldsSet(t: CustomType) {
    MkSetFieldsSet(t, currentModule)
  }

  override def mkValidate(t: CustomType) {
    MkValidate(t, currentModule)
  }

  override def mkRequiredMembersCtor(t: CustomType) {
    MkRequiredMembersCtor(t, currentModule)
  }

  override def mkNumFieldsSet(t: CustomType) {
    MkNumFieldsSet(t, currentModule)
  }

  override def mkClassEnd(t: CustomType) {
    CppGenUtils.mkClassEnd(t.shortName())
  }

  override def mkIsFieldSet(t: CustomType) {
    MkIsFieldSet(t, currentModule)
  }

}