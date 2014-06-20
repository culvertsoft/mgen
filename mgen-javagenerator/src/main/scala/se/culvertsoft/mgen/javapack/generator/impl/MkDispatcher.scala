package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.mapAsScalaMap

import Alias.typeIdStr16bit
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants

object MkDispatcher {

  def apply(referencedModules: Seq[Module], packagePath: String)(implicit txtBuffer: SuperStringBuffer): String = {

    val allTypes = referencedModules.flatMap(_.types).map(_._2).distinct
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    txtBuffer.clear()

    MkPackage(packagePath)

    ln(0, s"import ${JavaConstants.mgenBaseClsString};").endl()

    MkClassStart("Dispatcher", null)

    def mkIdentify() {

      ln(1, "public void dispatch(MGenBase o) {").endl()

      ln(2, "if (o==null) {")
      ln(3, "handleNull(o);")
      ln(3, "return;")
      ln(2, "}").endl()

      ln(2, "final short[] ids = o._typeIds16Bit();")
      ln(2, "int i = 0;")

      MkTypeIdSwitch.apply(true, 2, "handleUnknown(o);", topLevelTypes, typeIdStr16bit, t => s"handle((${t.fullName()})o);")

      ln(1, "}").endl()

    }

    def mkDefaultHandlers() {
      ln(1, s"protected void handleDiscard(MGenBase o) {}").endl()
      ln(1, s"protected void handleNull(MGenBase o) { handleDiscard(o); }").endl()
      ln(1, s"protected void handleUnknown(MGenBase o) { handleDiscard(o); }").endl()
    }

    def mkHandler(t: CustomType) {
      ln(1, s"protected void handle(${t.fullName} o) {")
      if (t.hasSuperType) {
        ln(2, s"handle((${t.superType.fullName})o);")
      } else {
        ln(2, s"handleDiscard(o);")
      }
      ln(1, s"}").endl()
    }

    def mkHandlers() {
      allTypes foreach mkHandler
    }
    
    mkDefaultHandlers()
    mkHandlers()
    mkIdentify()

    MkClassEnd()

    txtBuffer.toString()

  }

}