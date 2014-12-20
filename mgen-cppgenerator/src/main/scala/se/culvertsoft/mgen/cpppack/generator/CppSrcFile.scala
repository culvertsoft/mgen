package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
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

  override def mkIncludes(t: ClassType, generateCustomCodeSection: Boolean)(implicit txtBuffer: SourceCodeBuffer) {
    MkIncludes(t, generateCustomCodeSection)
  }

  override def mkConstants(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkConstants(t)
  }

  override def mkCustomPublicMethodsSection(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    ln(CppGenerator.custom_methods_section.toString)
    ln()
  }

  override def mkDefaultCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkDefaultCtor(t, t.module)
  }

  override def mkRequiredMembersCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkRequiredMembersCtor(t)
  }

  override def mkAllMembersCtor(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkAllMembersCtor(t)
  }

  override def mkFieldById(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkFieldById(t)
  }

  override def mkEqOperator(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkEqOperator(t)
  }

  override def mkEquals(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkEquals(t)
  }

  override def mkDeepCopy(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkDeepCopy(t)
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

  override def mkHasers(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkHasers(t)
  }

  override def mkNewInstance(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkNewInstance(t)
  }

  override def mkMetadataGetters(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkMetadataGetters(t)
  }

  override def mkMetaDataFields(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkMetadataFields(t)
  }

  override def mkSetFieldsSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkSetFieldsSet(t)
  }

  override def mkNumFieldsSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkNumFieldsSet(t)
  }

  override def mkValidate(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkValidate(t)
  }

  override def mkIsFieldSet(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {
    MkIsFieldSet(t)
  }

}