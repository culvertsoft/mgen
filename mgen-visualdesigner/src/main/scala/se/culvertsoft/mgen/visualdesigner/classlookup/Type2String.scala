package se.culvertsoft.mgen.visualdesigner.classlookup

import se.culvertsoft.mgen.visualdesigner.model.ArrayType
import se.culvertsoft.mgen.visualdesigner.model.BoolType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.Float32Type
import se.culvertsoft.mgen.visualdesigner.model.Float64Type
import se.culvertsoft.mgen.visualdesigner.model.Int16Type
import se.culvertsoft.mgen.visualdesigner.model.Int32Type
import se.culvertsoft.mgen.visualdesigner.model.Int64Type
import se.culvertsoft.mgen.visualdesigner.model.Int8Type
import se.culvertsoft.mgen.visualdesigner.model.ListType
import se.culvertsoft.mgen.visualdesigner.model.MapType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.NoType
import se.culvertsoft.mgen.visualdesigner.model.StringType
import se.culvertsoft.mgen.visualdesigner.model.Project

object Type2String {

  def defaltGetCustomTypeName(t: CustomTypeRef)(implicit model: Model): String = {
    model.getEntity(t.getId()) match {
      case Some(t) => t.getName()
      case None => "<unknown_type_reference>"
    }
  }

  def getClassPath(e: Entity)(implicit model: Model): String = {
    val parentPrefix = model.parentOf(e) match {
      case Some(parent: Project) => ""
      case Some(parent) => getClassPath(parent) + "."
      case _ => ""
    }
    parentPrefix + e.getName()
  }

  def getCustomTypeNameLong(t: CustomTypeRef)(implicit model: Model): String = {
    model.getEntity(t.getId()) match {
      case Some(e) => s"${getClassPath(e)}"
      case None => "<unknown_pgk>.<unknown_type_reference>"
    }
  }

  def apply(t: FieldType, fGetCustomTypeRefName: CustomTypeRef => String)(implicit model: Model): String = {
    t match {
      case t: BoolType => "bool"
      case t: Int8Type => "int8"
      case t: Int16Type => "int16"
      case t: Int32Type => "int32"
      case t: Int64Type => "int64"
      case t: Float32Type => "float32"
      case t: Float64Type => "float64"
      case t: StringType => "string"
      case t: NoType => "no_type"
      case t: ListType => s"list[${apply(t.getElementType(), fGetCustomTypeRefName)}]"
      case t: ArrayType => s"array[${apply(t.getElementType(), fGetCustomTypeRefName)}]"
      case t: MapType => s"map[${apply(t.getKeyType(), fGetCustomTypeRefName)}, ${apply(t.getValueType(), fGetCustomTypeRefName)}]"
      case t: CustomTypeRef => fGetCustomTypeRefName(t)
      case _ => ""
    }
  }

  def apply(t: FieldType)(implicit model: Model): String = {
    apply(t, defaltGetCustomTypeName)
  }

  def long(t: FieldType)(implicit model: Model): String = {
    apply(t, getCustomTypeNameLong)
  }

}
