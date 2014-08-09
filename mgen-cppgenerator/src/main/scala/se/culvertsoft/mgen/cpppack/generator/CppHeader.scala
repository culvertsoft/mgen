package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkAcceptVisitor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkAllMembersCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkConstants
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkDeepCopy
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkDefaultCtor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkDestructor
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkEqOperator
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkEquals
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkFieldById
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkGetters
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkHasers
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkIncludes
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkIsFieldSet
import se.culvertsoft.mgen.cpppack.generator.impl.classh.MkMembers
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

object CppHeader extends CppSrcFileOrHeader(".h") {

  override def mkIncludes(t: ClassType, genCustomCodeSections: Boolean) {
    MkIncludes(t, currentModule, genCustomCodeSections)
  }

  override def mkIncludeGuardStart(module: Module, t: ClassType) {
    CppGenUtils.mkIncludeGuardStart(t.fullName())
  }

  override def mkIncludeGuardEnd() {
    CppGenUtils.mkIncludeGuardEnd()
  }

  override def mkConstants(t: ClassType) {
    MkConstants(t, currentModule)
  }

  override def mkEqOperator(t: ClassType) {
    MkEqOperator(t, currentModule)
  }

  override def mkClassStart(t: ClassType, genCustomCodeSections: Boolean) {
    CppGenUtils.mkClassStart(t.shortName(), getSuperTypeNameString(t), genCustomCodeSections)
  }

  override def mkPrivate() {
    ln("private:")
  }

  override def mkPublic() {
    ln("public:")
  }

  override def mkDefaultCtor(t: ClassType) {
    MkDefaultCtor(t, currentModule)
  }

  override def mkAllMembersCtor(t: ClassType) {
    MkAllMembersCtor(t, currentModule)
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

  override def mkCustomPublicMethodsSection(t: ClassType) {
    ln(1, CppGenerator.custom_methods_section.toString)
    ln()
  }

  override def mkHasers(t: ClassType) {
    MkHasers(t, currentModule)
  }

  override def mkMembers(t: ClassType) {
    MkMembers(t, currentModule)
  }

  override def mkEquals(t: ClassType) {
    MkEquals(t, currentModule)
  }

  override def mkMetaDataFields(t: ClassType) {
    MkMetadataFields(t, currentModule)
  }

  override def mkReadField(t: ClassType) {
    MkReadField(t, currentModule)
  }

  override def mkAcceptVisitor(t: ClassType) {
    MkAcceptVisitor(t, currentModule)
  }

  override def mkFieldById(t: ClassType) {
    MkFieldById(t, currentModule)
  }

  override def mkMetadataGetters(t: ClassType) {
    MkMetadataGetters(t, currentModule)
  }

  override def mkNewInstance(t: ClassType) {
    MkNewInstance(t, currentModule)
  }

  override def mkDeepCopy(t: ClassType) {
    MkDeepCopy(t, currentModule)
  }

  override def mkUsingStatements(t: ClassType) {
    MkUsingStatements(t, currentModule)
  }

  override def mkSetFieldsSet(t: ClassType) {
    MkSetFieldsSet(t, currentModule)
  }

  override def mkValidate(t: ClassType) {
    MkValidate(t, currentModule)
  }

  override def mkRequiredMembersCtor(t: ClassType) {
    MkRequiredMembersCtor(t, currentModule)
  }

  override def mkNumFieldsSet(t: ClassType) {
    MkNumFieldsSet(t, currentModule)
  }

  override def mkClassEnd(t: ClassType) {
    CppGenUtils.mkClassEnd(t.shortName())
  }

  override def mkIsFieldSet(t: ClassType) {
    MkIsFieldSet(t, currentModule)
  }

}