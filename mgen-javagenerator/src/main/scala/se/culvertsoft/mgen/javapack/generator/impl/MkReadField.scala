package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.readerClsString
import se.culvertsoft.mgen.javapack.generator.JavaReadCalls.mkReadCall
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.declared

object MkReadField {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    val allFields = t.fieldsInclSuper()
    val needsSupress = allFields.map(_.typ().typeEnum()).find(e => e == TypeEnum.LIST || e == TypeEnum.MAP).isDefined

    if (needsSupress)
      ln(1, "@SuppressWarnings(\"unchecked\")")
    ln(1, "@Override")
    ln(1, s"public boolean _readField(final short fieldId,")
    ln(1, s"                         final Object context,")
    ln(1, s"                         final $readerClsString reader) throws java.io.IOException {")
    ln(2, s"switch(fieldId) {")
    for (field <- allFields) {
      ln(3, s"case (${fieldId(field)}):")
      ln(4, s"set${upFirst(field.name())}((${declared(field)})reader.${mkReadCall(field)}(_${field.name}_METADATA, context));")
      ln(4, "return true;")
    }
    ln(3, s"default:")
    ln(4, s"reader.handleUnknownField(null, context);")
    ln(4, s"return false;")
    ln(2, s"}")
    ln(1, "}").endl()

  }
}