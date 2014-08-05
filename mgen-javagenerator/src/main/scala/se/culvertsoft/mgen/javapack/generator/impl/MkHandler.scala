package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants

object MkHandler {

  def apply(referencedModules: Seq[Module], packagePath: String)(implicit txtBuffer: SuperStringBuffer): String = {

    val allClasses = referencedModules.flatMap(_.classes)

    txtBuffer.clear()

    MkFancyHeader.apply(null)

    MkPackage(packagePath)

    ln(0, s"import ${JavaConstants.mgenBaseClsString};").endl()

    MkClassStart("Handler", null)

    def mkDefaultHandlers() {
      ln(1, s"protected void handleDiscard(MGenBase o) {}").endl()
      ln(1, s"protected void handleNull(MGenBase o) { handleDiscard(o); }").endl()
      ln(1, s"protected void handleUnknown(MGenBase o) { handleDiscard(o); }").endl()
    }

    def mkHandler(t: ClassType) {
      ln(1, s"protected void handle(${t.fullName} o) {")
      if (t.hasSuperType) {
        ln(2, s"handle((${t.superType.fullName})o);")
      } else {
        ln(2, s"handleDiscard(o);")
      }
      ln(1, s"}").endl()
    }

    def mkHandlers() {
      allClasses foreach mkHandler
    }

    mkDefaultHandlers()
    mkHandlers()

    MkClassEnd()

    txtBuffer.toString()

  }

}