package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue
import se.culvertsoft.mgen.api.model.impl.ArrayTypeImpl
import se.culvertsoft.mgen.api.model.impl.LinkedCustomType
import se.culvertsoft.mgen.api.model.impl.ListTypeImpl
import se.culvertsoft.mgen.api.model.impl.MapTypeImpl
import se.culvertsoft.mgen.api.model.impl.ProjectImpl
import se.culvertsoft.mgen.api.model.impl.UnlinkedCustomType
import se.culvertsoft.mgen.compiler.defaultparser.ParseDefaultValue

object LinkTypes {

  def apply(project: ProjectImpl) {
    new Linkage(project).link()
  }
}

private class Linkage(root: ProjectImpl) {

  def link() {
    link(root)
  }

  private def replace(t: Type, owner: LinkedCustomType)(implicit lkup: TypeLookup): Type = {

    t match {
      case t: ArrayType =>
        new ArrayTypeImpl(replace(t.elementType, owner))
      case t: ListType =>
        new ListTypeImpl(replace(t.elementType, owner))
      case t: MapType =>
        new MapTypeImpl(replace(t.keyType, owner), replace(t.valueType, owner))
      case t: UnlinkedCustomType =>

        val found = lkup.find(t.writtenType)
        if (found.isEmpty) {
          throw new AnalysisException(s"Could not find any matching types for type ${t.writtenType} referenced in parent ${owner}")
        } else if (found.size == 1) {
          found.head
        } else {
          throw new AnalysisException(s"Ambigously referenced type ${t.writtenType} in type ${owner}")
        }

      case _ => t

    }

  }

  private def link(project: ProjectImpl) {

    implicit val typeLkup = new TypeLookup(project)
    val classes = project.modules.flatMap(_.types).collect { case t: LinkedCustomType => t }

    // Link fields and super types
    for (t <- classes) {
      if (t.hasSuperType()) {
        t.setSuperType(replace(t.superType, t).asInstanceOf[CustomType])
        t.superType() match {
          case s: LinkedCustomType => s.addSubType(t);
          case _ =>
        }
      }
      t.setFields(t.fields.map { f => f.transform(replace(f.typ, t)) })
    }

    // Link default values
    for (t <- classes) {

      val newFields = t.fields.map { f =>
        f.defaultValue match {
          case d: UnlinkedDefaultValue => f.transform(ParseDefaultValue(f.typ, d.writtenString, t.module))
          case _ => f
        }
      }

      t.setFields(newFields)

    }

  }

}