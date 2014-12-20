package se.culvertsoft.mgen.cpppack.generator.impl.utilh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkTypeIdSwitch {

  def apply(
    transform: String => String,
    addBreak: Boolean,
    nTabs: Int,
    defaultValue: String,
    possibleTypes: Seq[ClassType],
    caser: ClassType => String,
    returner: ClassType => String)(implicit txtBuffer: SourceCodeBuffer) {

    ln(nTabs, s"switch(i < ids.size() ? ${transform("ids[i++]")} : mgen::ClassRegistryBase::INVALID_16BIT_ID) {")

    for (t <- possibleTypes) {

      ln(nTabs + 1, s"case ${caser(t)}:")

      if (t.subTypes().nonEmpty) {
        apply(transform, addBreak, nTabs + 2, returner(t), t.subTypes(), caser, returner);
      } else {
        ln(nTabs + 2, returner(t))
      }
      if (addBreak)
        ln(nTabs + 2, "break;")
    }

    ln(nTabs + 1, "default:")
    ln(nTabs + 2, defaultValue)
    if (addBreak)
      ln(nTabs + 2, "break;")
    ln(nTabs, "}")

  }

}