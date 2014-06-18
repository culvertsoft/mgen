package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.fieldId
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstants.readerClsString
import se.culvertsoft.mgen.javapack.generator.JavaReadCalls.mkReadCall
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.getTypeName

object MkReadField {

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allFields = t.getAllFieldsInclSuper()
    val needsSupress = allFields.map(_.typ().typeEnum()).find(e => e == TypeEnum.LIST || e == TypeEnum.MAP).isDefined

    if (needsSupress)
      txtBuffer.tabs(1).textln("@SuppressWarnings(\"unchecked\")")
    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln(s"public boolean _readField(final $fieldClsString field,")
    txtBuffer.tabs(1).textln(s"                         final Object context,")
    txtBuffer.tabs(1).textln(s"                         final $readerClsString reader) throws java.io.IOException {")
    txtBuffer.tabs(2).textln(s"switch(field.id()) {")
    for (field <- allFields) {
      txtBuffer.tabs(3).textln(s"case (${fieldId(field)}):")
      txtBuffer.tabs(4).textln(s"set${upFirst(field.name())}((${getTypeName(field.typ())})reader.${mkReadCall(field)}(field, context));")
      txtBuffer.tabs(4).textln("return true;")
    }
    txtBuffer.tabs(3).textln(s"default:")
    txtBuffer.tabs(4).textln(s"reader.handleUnknownField(field, context);")
    txtBuffer.tabs(4).textln(s"return false;")
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln("}").endl()

  }
}