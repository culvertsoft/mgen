package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkTypeIdSwitch {

  def apply(
    transform: String => String,
    outOfBoundsCase: String,
    addBreak: Boolean,
    nTabs: Int,
    defaultValue: String,
    possibleTypes: Seq[ClassType],
    caser: ClassType => String,
    returner: ClassType => String,
    depth: Int = 0)(implicit txtBuffer: SourceCodeBuffer) {

    ln(nTabs, s"switch((i < ids.length ? ${transform("ids[i++]")} : $outOfBoundsCase)) {")

    for (t <- possibleTypes) {

      ln(nTabs + 1, s"case ${caser(t)}:")

      if (t.subTypes().nonEmpty) {
        apply(transform, outOfBoundsCase, addBreak, nTabs + 2, returner(t), t.subTypes(), caser, returner, depth + 1);
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