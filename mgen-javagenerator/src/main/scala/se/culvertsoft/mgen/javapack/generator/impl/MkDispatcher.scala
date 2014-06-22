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
    
    MkFancyHeader.apply(null)

    MkPackage(packagePath)

    ln(0, s"import ${JavaConstants.mgenBaseClsString};").endl()

    MkClassStart("Dispatcher", null)

    def mkDispatch() {

      ln(1, "public static void dispatch(MGenBase o, Handler handler) {").endl()

      ln(2, "if (o==null) {")
      ln(3, "handler.handleNull(o);")
      ln(3, "return;")
      ln(2, "}").endl()

      ln(2, "final short[] ids = o._typeIds16Bit();")
      ln(2, "int i = 0;")

      MkTypeIdSwitch.apply(s => s"(int)$s", "0xFFFFFFFF", true, 2, "handler.handleUnknown(o);", topLevelTypes, typeIdStr16bit, t => s"handler.handle((${t.fullName()})o);")

      ln(1, "}").endl()

    }

    mkDispatch()

    MkClassEnd()

    txtBuffer.toString()

  }

}