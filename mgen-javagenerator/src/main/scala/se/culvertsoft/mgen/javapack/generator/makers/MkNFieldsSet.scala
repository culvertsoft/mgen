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

object MkNFieldsSet {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public int _nFieldsSet(final ${fieldSetDepthClsString} fieldSetDepth) {")
    txtBuffer.tabs(2).textln(s"int out = 0;")
    for (field <- allFields)
      txtBuffer.tabs(2).textln(s"out += ${isFieldSet(field, "fieldSetDepth")} ? 1 : 0;")
    txtBuffer.tabs(2).textln(s"return out;")
    txtBuffer.tabs(1).textln("}").endl()

  }
}