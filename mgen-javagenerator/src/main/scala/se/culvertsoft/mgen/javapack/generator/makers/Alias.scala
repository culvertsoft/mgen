package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.CustomType

object Alias {

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

  def get(field: Field, preParan: String = ""): String = {
    s"get${upFirst(field.name())}${preParan}()"
  }

  def set(field: Field, input: String): String = {
    s"set${upFirst(field.name())}($input)"
  }

  def fieldId(field: Field): String = {
    s"_${field.name()}_ID"
  }

  def fieldMetadata(field: Field): String = {
    s"_${field.name()}_METADATA"
  }

  def typeIdStr16bit(t: CustomType): String = {
    s"${t.fullName()}._TYPE_ID_16BIT"
  }

  def name(t: CustomType): String = {
    s"${t.fullName()}._TYPE_NAME"
  }

  def typeIdStr16BitBase64(t: CustomType): String = {
    s"${t.fullName()}._TYPE_ID_16BIT_BASE64"
  }

  def typeIdStr(t: CustomType): String = {
    s"${t.fullName()}._TYPE_ID"
  }

  def instantiate(t: CustomType): String = {
    s"new ${t.fullName()}()"
  }
  
}