package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkPackage {

  def apply(packagePath: String)(implicit txtBuffer: SourceCodeBuffer) {
    txtBuffer.textln(s"package $packagePath;").endl()
  }

  def apply(module: Module)(implicit txtBuffer: SourceCodeBuffer) {
    apply(module.path())
  }

}