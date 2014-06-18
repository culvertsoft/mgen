package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkPackage {

  def apply(packagePath: String)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.text(s"package ${packagePath};").endl2()
  }

  def apply(module: Module)(implicit txtBuffer: SuperStringBuffer) {
    apply(module.path())
  }

}