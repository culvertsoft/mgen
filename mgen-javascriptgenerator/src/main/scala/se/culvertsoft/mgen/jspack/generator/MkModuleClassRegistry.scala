package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkModuleClassRegistry {

  def apply(m: Module)(implicit txtBuffer: SuperStringBuffer) : String = {
    txtBuffer textln("Class") textln(m.toString)
  }


}


//
//transform: String => String,
//nTabs: Int = 0,
//defaultValue: String,
//caseType: Seq[CustomType],
//returner: CustomType => String,
//depth: Int = 0)(implicit txtBuffer: SuperStringBuffer) {
//
//ln(nTabs, s"switch() {")
//
//for (t <- possibleTypes) {
//
//ln(nTabs + 1, s"case $t}:")
//
//if (t.subTypes().nonEmpty) {
//apply(transform,  nTabs + 2, returner(t), t.subTypes(), returner, depth + 1);
//} else {
//ln(nTabs + 2, returner(t))
//}
//if (addBreak)
//ln(nTabs + 2, "break;")
//}
//
//ln(nTabs + 1, "default:")
//ln(nTabs + 2, defaultValue)
//if (addBreak)
//ln(nTabs + 2, "break;")
//ln(nTabs, "}")