package se.culvertsoft.mgen.cpppack.generator

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.UserDefinedType
import se.culvertsoft.mgen.compiler.internal.FancyHeaders
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cppgenerator.BuildVersion

object CppGenUtils {

  def mkNameSpaces(namespaces: Seq[String])(implicit txtBuffer: SuperStringBuffer) {
    for (namespace <- namespaces)
      txtBuffer.textln(s"namespace $namespace {")
    txtBuffer.endl()
  }

  def include(path: String)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln("#include \"" + path + "\"")
  }

  def includeT(path: String)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln("#include <" + path + ">")
  }

  def getSuperTypeString(t: ClassType): String = {
    if (!t.hasSuperType())
      "mgen::MGenBase"
    else {
      val superModule = t.superType().module()
      if (superModule == t.module())
        t.superType().shortName()
      else
        t.superType().fullName().replaceAllLiterally(".", "::")
    }
  }

  def include(t: UserDefinedType, fileEnding: String = ".h")(implicit txtBuffer: SuperStringBuffer) {
    include(t.fullName().replaceAllLiterally(".", "/") + fileEnding)
  }

  def mkNameSpacesEnd(namespaces: Seq[String])(implicit txtBuffer: SuperStringBuffer) {
    for (namespace <- namespaces.reverse)
      txtBuffer.textln(s"} // End namespace $namespace")
    txtBuffer.endl()
  }

  def mkFancyHeader()(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(FancyHeaders.fileHeader(BuildVersion.GIT_TAG + " " + BuildVersion.GIT_COMMIT_DATE)).endl()
  }

  def getIncludeGuardTypeString(typeName: String): String = {
    typeName.replaceAllLiterally(".", "_").replaceAllLiterally("::", "_").toUpperCase()
  }

  def mkIncludeGuardStart(fullTypeName: String)(implicit txtBuffer: SuperStringBuffer) {
    val includeGuardString = getIncludeGuardTypeString(fullTypeName)
    txtBuffer.textln(s"#ifndef $includeGuardString")
    txtBuffer.textln(s"#define $includeGuardString")
    txtBuffer.endl()
  }

  def mkIncludeGuardEnd()(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(s"#endif")
  }

  def mkClassStart(thisClsName: String, superClsName: String = "", genCustomCodeSections: Boolean = false)(implicit txtBuffer: SuperStringBuffer) {

    val customCodeSection = if (genCustomCodeSections) CppGenerator.custom_interfaces_section else ""

    if (superClsName != null && superClsName.nonEmpty)
      txtBuffer.textln(s"class $thisClsName : public $superClsName $customCodeSection {")
    else
      txtBuffer.textln(s"class $thisClsName $customCodeSection {")
  }

  def mkClassEnd(thisClsName: String)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(s"}; // End class $thisClsName").endl()
  }

}