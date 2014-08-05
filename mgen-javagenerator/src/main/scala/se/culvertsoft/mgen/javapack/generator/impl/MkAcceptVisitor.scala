package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldMetadata
import Alias.get
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldVisitorClsString

object MkAcceptVisitor {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()
    val nonTransientFields = allFields.filterNot(_.isTransient)
    val haveTransientFields = allFields.size != nonTransientFields.size

    ln(1, "@Override")
    ln(1, s"public void _accept(final ${fieldVisitorClsString} visitor, final FieldVisitSelection selection) throws java.io.IOException {")

    ln(2, s"switch(selection) {")
    ln(3, s"case ALL: {")
    ln(4, s"visitor.beginVisit(this, ${allFields.size});")
    for (field <- allFields)
      ln(4, s"visitor.visit(${get(field)}, ${fieldMetadata(field)});")
    ln(4, s"visitor.endVisit();")
    ln(4, s"break;")
    ln(3, s"}")
    if (haveTransientFields) {
      ln(3, s"case ALL_SET: {")
      ln(4, s"visitor.beginVisit(this, _nFieldsSet(FieldSetDepth.SHALLOW, true));")
      for (field <- allFields) {
        ln(4, s"if (${isFieldSet(field, "FieldSetDepth.SHALLOW")})")
        ln(5, s"visitor.visit(${get(field)}, ${fieldMetadata(field)});")
      }
      ln(4, s"visitor.endVisit();")
      ln(4, s"break;")
      ln(3, s"}")
    }
    ln(3, s"default /* case ALL_SET_NONTRANSIENT */ : {")
    ln(4, s"visitor.beginVisit(this, _nFieldsSet(FieldSetDepth.SHALLOW, false));")
    for (field <- nonTransientFields) {
      ln(4, s"if (${isFieldSet(field, "FieldSetDepth.SHALLOW")})")
      ln(5, s"visitor.visit(${get(field)}, ${fieldMetadata(field)});")
    }
    ln(4, s"visitor.endVisit();")
    ln(4, s"break;")
    ln(3, s"}")
    ln(2, s"}")

    ln(1, "}").endl()

  }
}