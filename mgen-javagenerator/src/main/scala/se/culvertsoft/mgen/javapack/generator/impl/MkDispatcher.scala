package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.typeIdStr16bit
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants

object MkDispatcher {

  def apply(referencedModules: Seq[Module], packagePath: String)(implicit txtBuffer: SourceCodeBuffer): String = {

    val allClasses = referencedModules.flatMap(_.classes)
    val topLevelClasses = allClasses.filterNot(_.hasSuperType)

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

      MkTypeIdSwitch.apply(s => s"(int)$s", "0xFFFFFFFF", true, 2, "handler.handleUnknown(o);", topLevelClasses, typeIdStr16bit, t => s"handler.handle((${t.fullName()})o);")

      ln(1, "}").endl()

    }

    mkDispatch()

    MkClassEnd()

    txtBuffer.toString()

  }

}