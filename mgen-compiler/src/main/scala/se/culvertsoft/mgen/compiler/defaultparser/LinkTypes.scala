package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue
import se.culvertsoft.mgen.api.model.impl.ArrayTypeImpl
import se.culvertsoft.mgen.api.model.impl.LinkedCustomType
import se.culvertsoft.mgen.api.model.impl.ListTypeImpl
import se.culvertsoft.mgen.api.model.impl.MapTypeImpl
import se.culvertsoft.mgen.api.model.impl.ProjectImpl
import se.culvertsoft.mgen.api.model.impl.UnlinkedCustomType

object LinkTypes {

  def apply(project: ProjectImpl)(implicit cache: ParseState) {
    val linkage = new Linkage(project)
    linkage.link()
  }
}

private class Linkage(root: ProjectImpl)(implicit cache: ParseState) {

  def link() {
    link(root)
  }

  private def replace(t: Type)(implicit parent: LinkedCustomType): Type = {

    val out = t.typeEnum() match {
      case TypeEnum.ARRAY =>
        val arrayType = t.asInstanceOf[ArrayType]
        new ArrayTypeImpl(replace(arrayType.elementType()))
      case TypeEnum.LIST =>
        val listType = t.asInstanceOf[ListType]
        new ListTypeImpl(replace(listType.elementType()))
      case TypeEnum.MAP =>
        val mapType = t.asInstanceOf[MapType]
        new MapTypeImpl(replace(mapType.keyType()), replace(mapType.valueType()))
      case TypeEnum.UNKNOWN =>

        val unknownType = t.asInstanceOf[UnlinkedCustomType]
        val written = unknownType.writtenType

        val fullNames = cache.typeLookup.typesFullName
        val shortNames = cache.typeLookup.typesShortName

        if (fullNames.contains(written)) {
          fullNames(written)
        } else if (shortNames.contains(written)) {
          val matches = shortNames(written)
          matches.find(_.module() == parent.module()) match {
            case Some(replacement) =>
              replacement
            case _ =>
              if (matches.size > 1)
                throw new AnalysisException(s"Ambigously referenced type ${written} in type ${parent}")
              matches.head
          }
        } else {
          throw new AnalysisException(s"Could not find any matching types for type ${written} referenced in parent ${parent}")
        }

      case _ => t

    }

    out
  }

  private def link(project: ProjectImpl) {

    // Link fields and super types of unfinished types
    for (t <- cache.needLinkage.types) {
      implicit val parent = t
      if (t.hasSuperType())
        t.setSuperType(replace(t.superType).asInstanceOf[CustomType])
      t.setFields(t.fields.map { f => f.transform(replace(f.typ)) })
    }

    // Link super types
    for (t <- cache.typeLookup.typesFullName.values) {
      t match {
        case t: CustomType =>
          t.superType() match {
            case s: LinkedCustomType => s.addSubType(t);
            case _ =>
          }
        case _ =>
      }
    }

    // Link default values
    val newFields = new ArrayBuffer[Field]

    for (t <- cache.needLinkage.types) {
      val m = t.module()

      newFields.clear()

      for (f <- t.fields) {
        if (!f.isLinked()) {
          val src = f.defaultValue().asInstanceOf[UnlinkedDefaultValue]
          newFields += f.transform(DefaultValue.parse(f.typ, src.writtenString, m))
        } else {
          newFields += f
        }
      }

      t.setFields(newFields)

    }

  }

}