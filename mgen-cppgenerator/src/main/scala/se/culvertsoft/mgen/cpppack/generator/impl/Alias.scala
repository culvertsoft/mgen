package se.culvertsoft.mgen.cpppack.generator.impl

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst

object Alias {

  def get(field: Field): String = {
    s"get${upFirst(field.name())}()"
  }

  def getMutable(field: Field): String = {
    s"get${upFirst(field.name())}Mutable()"
  }

  def set(field: Field, input: String): String = {
    s"set${upFirst(field.name())}($input)"
  }

  def typeIdString(t: CustomType): String = {
    "_type_id"
  }

  def typeId16BitString(t: CustomType): String = {
    "_type_id_16bit"
  }

  def typeId16BitBase64String(t: CustomType): String = {
    "_type_id_16bit_base64"
  }

  def fieldIdString(field: Field): String = {
    s"_field_${field.name()}_id"
  }

  def fieldMetaString(field: Field, inclParan: Boolean = true): String = {
    if (inclParan)
      s"_field_${field.name()}_metadata()"
    else
      s"_field_${field.name()}_metadata"
  }

  def isSetName(f: Field): String = {
    s"_m_${f.name()}_isSet"
  }

  def setFieldSetName(f: Field): String = {
    s"_set${upFirst(f.name())}Set"
  }

  def isFieldSet(f: Field, input: String): String = {
    s"_is${upFirst(f.name())}Set($input)"
  }

  def setFieldSet(f: Field, input: String): String = {
    s"${setFieldSetName(f)}($input)"
  }

}