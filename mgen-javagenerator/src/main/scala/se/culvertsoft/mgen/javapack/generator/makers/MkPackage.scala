package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.api.model.Module

object MkPackage {
  import BuiltInGeneratorUtil._
  import JavaConstants._

  def apply(packagePath: String)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.text(s"package ${packagePath};").endl2()
  }

  def apply(module: Module)(implicit txtBuffer: SuperStringBuffer) {
    apply(module.path())
  }
  
}