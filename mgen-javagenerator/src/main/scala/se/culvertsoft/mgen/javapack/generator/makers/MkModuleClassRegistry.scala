package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.javapack.generator.JavaConstruction._
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames._
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.javapack.generator.JavaToString

object MkModuleClassRegistry {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(module: Module)(implicit txtBuffer: SuperStringBuffer): String = {

    implicit val m = module


    txtBuffer.clear()

    MkPackage(module.path())

    txtBuffer.textln("import se.culvertsoft.mgen.javapack.classes.Ctor;")
    txtBuffer.textln("import se.culvertsoft.mgen.javapack.classes.MGenBase;")
    txtBuffer.endl()

    MkClassStart("MGenModuleClassRegistry", clsRegistryClsString)

    txtBuffer.tabs(1).textln("public MGenModuleClassRegistry() {")
    for ((_, typ) <- module.types()) {
      txtBuffer.tabs(2).textln(s"add(")
      txtBuffer.tabs(3).textln(s"${typ.typeId}L,")
      txtBuffer.tabs(3).textln(s"${quote(typ.fullName())},")
      txtBuffer.tabs(3).textln(s"new Ctor() { public MGenBase create() { return new ${typ.fullName()}(); } }")
      txtBuffer.tabs(2).textln(s");")
    }
    txtBuffer.tabs(1).textln("}")

    MkClassEnd()

    txtBuffer.toString()
  }
}