package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._

object MkAcceptVisitor {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.getAllFieldsInclSuper()

    txtBuffer.tabs(1).textln(s"template<typename VisitorType>")
    txtBuffer.tabs(1).textln(s"void _accept(VisitorType& visitor) const {")
    txtBuffer.tabs(2).textln(s"visitor.beginVisit(*this, _numFieldsSet(mgen::SHALLOW));")
    for (field <- allFields) {
      txtBuffer.tabs(2).textln(s"visitor.visit(${get(field)}, ${fieldMetaString(field)}, ${isFieldSet(field, "mgen::SHALLOW")});")
    }
    txtBuffer.tabs(2).textln(s"visitor.endVisit();")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()

    txtBuffer.tabs(1).textln(s"template<typename VisitorType>")
    txtBuffer.tabs(1).textln(s"void _accept(VisitorType& visitor) {")
    txtBuffer.tabs(2).textln(s"visitor.beginVisit(*this, _numFieldsSet(mgen::SHALLOW));")
    for (field <- allFields) {
      txtBuffer.tabs(2).textln(s"visitor.visit(${getMutable(field)}, ${fieldMetaString(field)}, ${isFieldSet(field, "mgen::SHALLOW")});")
    }
    txtBuffer.tabs(2).textln(s"visitor.endVisit();")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()

  }

}