package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkTypeIdSwitch {

  def apply(
    addBreak: Boolean,
    nTabs: Int,
    defaultValue: String,
    possibleTypes: Seq[CustomType],
    caser: CustomType => String,
    returner: CustomType => String)(implicit txtBuffer: SuperStringBuffer) {

    ln(nTabs, "switch(ids[i++]) {")

    for (t <- possibleTypes) {

      ln(nTabs + 1, s"case ${caser(t)}:")

      if (t.subTypes().nonEmpty) {
        ln(nTabs + 2, s"if (i == ids.length) {")
        ln(nTabs + 3, returner(t))
        if (addBreak)
          ln(nTabs + 3, "break;")
        ln(nTabs + 2, s"}")
        apply(addBreak, nTabs + 2, returner(t), t.subTypes(), caser, returner);
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