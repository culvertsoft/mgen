package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.impl.Alias._

object MkReadField {

  def apply(
    t: CustomType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    val allFields = t.fieldsInclSuper()

    txtBuffer.tabs(1).textln(s"template<typename ReaderType, typename ReadContextType>")
    txtBuffer.tabs(1).textln(s"void _readField(const short fieldId, ReadContextType& context, ReaderType& reader) {")
    txtBuffer.tabs(2).textln(s"switch (fieldId) {")
    for (field <- allFields) {
      txtBuffer.tabs(2).textln(s"case ${fieldIdString(field)}:")
      txtBuffer.tabs(3).textln(s"reader.readField(${fieldMetaString(field)}, context, ${getMutable(field)});")
      txtBuffer.tabs(3).textln(s"break;")
    }
    txtBuffer.tabs(2).textln(s"default:")
    txtBuffer.tabs(3).textln(s"reader.handleUnknownField(fieldId, context);");
    txtBuffer.tabs(2).textln(s"}")
    txtBuffer.tabs(1).textln(s"}")
    txtBuffer.endl()

  }

}