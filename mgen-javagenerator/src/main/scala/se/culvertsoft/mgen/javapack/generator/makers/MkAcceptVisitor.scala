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

object MkAcceptVisitor {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public void _accept(final ${fieldVisitorClsString} visitor) throws java.io.IOException {")
    txtBuffer.tabs(2).textln(s"visitor.beginVisit(this, _nFieldsSet(${fieldSetDepthClsString}.SHALLOW));")
    for (field <- allFields) {
      txtBuffer.tabs(2).textln(s"visitor.visit(${get(field)}, ${fieldMetadata(field)}, ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")});")
    }
    txtBuffer.tabs(2).textln(s"visitor.endVisit();")
    txtBuffer.tabs(1).textln("}").endl()

  }
}