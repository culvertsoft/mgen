package se.culvertsoft.mgen.visualdesigner.view.autobox2

import scala.collection.JavaConversions.collectionAsScalaIterable

import se.culvertsoft.mgen.visualdesigner.MGenClassRegistry
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeRef
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.GenericType
import se.culvertsoft.mgen.visualdesigner.model.ListOrArrayType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.NoType
import se.culvertsoft.mgen.visualdesigner.model.PrimitiveType
import se.culvertsoft.mgen.visualdesigner.model.SimpleType

object FieldTypes {

  val cr = new MGenClassRegistry()

  val simpleTypeEntries = cr.entries()
    .filter(e => classOf[SimpleType].isAssignableFrom(e.cls()))
    .filter { e => e.cls() != classOf[SimpleType] && e.cls() != classOf[PrimitiveType] }
    .toSeq
    .sortBy(_.cls().getSimpleName())

  val genericTypeEntries = cr.entries()
    .filter(e => classOf[GenericType].isAssignableFrom(e.cls()))
    .filter { e => e.cls() != classOf[GenericType] && e.cls() != classOf[ListOrArrayType] }
    .toSeq
    .sortBy(_.cls().getSimpleName())

  def simpleTypes(): Seq[FieldType] = {
    simpleTypeEntries
      .map(_.construct())
      .map(_.asInstanceOf[FieldType])
  }

  def genericTypeTemplates(): Seq[FieldType] = {
    genericTypeEntries
      .map(_.construct())
      .map(_.asInstanceOf[FieldType])
  }

  def getSuperType(t: CustomType)(implicit model: Model): FieldType = {
    if (t.hasSuperType()) {
      model.superTypeOf(t) match {
        case Some(superType) => new CustomTypeRef(superType.getId())
        case None => new NoType
      }
    } else {
      new NoType
    }
  }

}