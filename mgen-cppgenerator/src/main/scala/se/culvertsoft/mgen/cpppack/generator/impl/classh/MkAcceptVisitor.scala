package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.endl
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.fieldMetaString
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.get
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.getMutable
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isFieldSet

object MkAcceptVisitor {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    val allFields = t.fieldsInclSuper()
    val nonTransientFields = allFields.filterNot(_.isTransient)
    val haveTransientFields = allFields.size != nonTransientFields.size

    def mkContents(getter: Field => String) {
      ln(2, s"switch(selection) {")
      ln(3, s"case mgen::ALL: {")
      ln(4, s"visitor.beginVisit(*this, ${allFields.size});")
      for (field <- allFields)
        ln(4, s"visitor.visit(${getter(field)}, ${fieldMetaString(field)});")
      ln(4, s"visitor.endVisit();")
      ln(4, s"break;")
      ln(3, s"}")
      if (haveTransientFields) {
        ln(3, s"case mgen::ALL_SET: {")
        ln(4, s"visitor.beginVisit(*this, _numFieldsSet(mgen::SHALLOW, true));")
        for (field <- allFields) {
          ln(4, s"if (${isFieldSet(field, "mgen::SHALLOW")})")
          ln(5, s"visitor.visit(${getter(field)}, ${fieldMetaString(field)});")
        }
        ln(4, s"visitor.endVisit();")
        ln(4, s"break;")
        ln(3, s"}")
      }
      ln(3, s"default /* case mgen::ALL_SET_NONTRANSIENT */ : {")
      ln(4, s"visitor.beginVisit(*this, _numFieldsSet(mgen::SHALLOW, false));")
      for (field <- nonTransientFields) {
        ln(4, s"if (${isFieldSet(field, "mgen::SHALLOW")})")
        ln(5, s"visitor.visit(${getter(field)}, ${fieldMetaString(field)});")
      }
      ln(4, s"visitor.endVisit();")
      ln(4, s"break;")
      ln(3, s"}")
      ln(2, s"}")
    }

    ln(1, s"template<typename VisitorType>")
    ln(1, s"void _accept(VisitorType& visitor, const mgen::FieldVisitSelection selection) const {")
    mkContents(get)
    ln(1, s"}")
    endl()

    ln(1, s"template<typename VisitorType>")
    ln(1, s"void _accept(VisitorType& visitor, const mgen::FieldVisitSelection selection) {")
    mkContents(getMutable)
    ln(1, s"}")
    endl()

  }

}