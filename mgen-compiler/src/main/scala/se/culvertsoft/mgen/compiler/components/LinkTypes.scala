package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Constant
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue
import se.culvertsoft.mgen.api.model.UnlinkedType

object LinkTypes {

  def apply(project: Project) {
    link(project)
  }

  private def replace(t: Type, owner: ClassType)(implicit lkup: TypeLookup): Type = {

    t match {
      case t: ArrayType =>
        new ArrayType(replace(t.elementType, owner))
      case t: ListType =>
        new ListType(replace(t.elementType, owner))
      case t: MapType =>
        new MapType(replace(t.keyType, owner), replace(t.valueType, owner))
      case t: UnlinkedType =>

        val found = lkup.find(t.name)
        if (found.isEmpty) {
          throw new AnalysisException(s"Could not find any matching types for type ${t.name} referenced in parent ${owner}")
        } else if (found.size == 1) {
          found.head
        } else {

          found.find(_.module == owner.module) match {
            case Some(x) => x
            case _ =>
              throw new AnalysisException(s"Ambigously referenced type ${t.name} in type ${owner}")
          }

        }

      case _ => t

    }

  }

  private def link(project: Project) {

    implicit val typeLkup = new TypeLookup(project)
    val classes = project.allModulesRecursively.flatMap(_.classes)

    // Link fields and super types
    for (t <- classes) {
      if (t.hasSuperType()) {
        val newSuperType = replace(t.superType, t).asInstanceOf[ClassType]
        t.setSuperType(newSuperType)
        newSuperType.addSubType(t)
      }
      t.setFields(t.fields.map { f => f.transform(replace(f.typ, t)) })
    }

    // Link default values
    for (t <- classes) {

      val newFields = t.fields.map { f =>
        f.defaultValue match {
          case d: UnlinkedDefaultValue => f.transform(d.parse(f.typ, t.module))
          case _ => f
        }
      }

      t.setFields(newFields)

    }

    // Move static constants -> constants list
    for (t <- classes) {

      val (statics, newFields) = t.fields().partition(_.isStatic())
      t.setFields(newFields)

      for (s <- statics) {
        if (!s.hasDefaultValue)
          throw new AnalysisException(s"Field $s was specified as a static constant but lacks value")
        val c = new Constant(s.name, t, s.typ, s.defaultValue)
        println(c)
        t.addConstant(c)
      }

    }

  }

}