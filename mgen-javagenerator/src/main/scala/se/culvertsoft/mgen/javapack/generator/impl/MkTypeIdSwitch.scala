package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkTypeIdSwitch {

  def apply(
    nTabs: Int,
    defaultValue: String,
    possibleTypes: Seq[CustomType],
    caser: CustomType => String,
    returner: CustomType => String)(implicit txtBuffer: SuperStringBuffer) {

    txtBuffer.tabs(nTabs).textln("switch(ids[i++]) {")

    for (t <- possibleTypes) {

      txtBuffer.tabs(nTabs + 1).textln(s"case ${caser(t)}:")

      if (t.subTypes().nonEmpty) {
        txtBuffer.tabs(nTabs + 2).textln(s"if (i == ids.length) return ${returner(t)};")
        apply(nTabs + 2, returner(t), t.subTypes(), caser, returner);
      } else {
        txtBuffer.tabs(nTabs + 2).textln(s"return ${returner(t)};")
      }
    }

    txtBuffer.tabs(nTabs + 1).textln("default:")
    txtBuffer.tabs(nTabs + 2).textln(s"return $defaultValue;")
    txtBuffer.tabs(nTabs).textln("}")

  }

}