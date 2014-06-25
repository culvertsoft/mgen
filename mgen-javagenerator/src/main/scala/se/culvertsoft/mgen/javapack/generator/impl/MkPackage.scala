package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

object MkPackage {

  def apply(packagePath: String)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.textln(s"package $packagePath;").endl()
  }

  def apply(module: Module)(implicit txtBuffer: SuperStringBuffer) {
    apply(module.path())
  }

}