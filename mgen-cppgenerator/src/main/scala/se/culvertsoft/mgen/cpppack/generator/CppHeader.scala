package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
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

  override def mkIncludes(t: ClassType, genCustomCodeSections: Boolean)(implicit txtBuffer: SourceCodeBuffer) {
    MkIncludes(t, genCustomCodeSections)
  }

  override def mkIncludeGuardStart(module: Module, t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.mkIncludeGuardStart(t.fullName())
  }

  override def mkIncludeGuardEnd()(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.mkIncludeGuardEnd()
  }

  override def mkConstants(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkConstants(t)
  }

  override def mkEqOperator(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkEqOperator(t)
  }

  override def mkClassStart(t: ClassType, genCustomCodeSections: Boolean)(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.mkClassStart(t.shortName(), getSuperTypeNameString(t), genCustomCodeSections)
  }

  override def mkPrivate()(implicit txtBuffer: SourceCodeBuffer) {
    ln("private:")
  }

  override def mkPublic()(implicit txtBuffer: SourceCodeBuffer) {
    ln("public:")
  }

  override def mkDefaultCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkDefaultCtor(t)
  }

  override def mkAllMembersCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkAllMembersCtor(t)
  }

  override def mkDestructor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkDestructor(t)
  }

  override def mkGetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkGetters(t)
  }

  override def mkSetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkSetters(t)
  }

  override def mkCustomPublicMethodsSection(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    ln(1, CppGenerator.custom_methods_section.toString)
    ln()
  }

  override def mkHasers(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkHasers(t)
  }

  override def mkMembers(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkMembers(t)
  }

  override def mkEquals(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkEquals(t)
  }

  override def mkMetaDataFields(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkMetadataFields(t)
  }

  override def mkReadField(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkReadField(t)
  }

  override def mkAcceptVisitor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkAcceptVisitor(t)
  }

  override def mkFieldById(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkFieldById(t)
  }

  override def mkMetadataGetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkMetadataGetters(t)
  }

  override def mkNewInstance(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkNewInstance(t)
  }

  override def mkDeepCopy(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkDeepCopy(t)
  }

  override def mkUsingStatements(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkUsingStatements(t)
  }

  override def mkSetFieldsSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkSetFieldsSet(t)
  }

  override def mkValidate(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkValidate(t)
  }

  override def mkRequiredMembersCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkRequiredMembersCtor(t)
  }

  override def mkNumFieldsSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkNumFieldsSet(t)
  }

  override def mkClassEnd(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    CppGenUtils.mkClassEnd(t.shortName())
  }

  override def mkIsFieldSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkIsFieldSet(t)
  }

}