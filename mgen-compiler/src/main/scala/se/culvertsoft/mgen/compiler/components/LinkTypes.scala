package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Constant
import se.culvertsoft.mgen.api.model.DefaultValue
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

      case null => throw new AnalysisException("Cannot provide null type to type")

      case _ => t

    }

  }

  private def link(project: Project) {

    implicit val typeLkup = new TypeLookup(project)
    val classes = project.allModulesRecursively.flatMap(_.classes)

    // Link types
    for (t <- classes) {
      if (t.hasSuperType()) {
        val newSuperType = replace(t.superType, t).asInstanceOf[ClassType]
        t.setSuperType(newSuperType)
        newSuperType.addSubType(t)
      }
      t.setFields(t.fields.map { f => f.transform(replace(f.typ, t)) })
      for (c <- t.constants) {
        if (c.typ == null)
          throw new AnalysisException(s"Provided no type for parsed constant $c in type $t")
        c.setType(replace(c.typ, t))
      }
    }

    // Move constants to the constants list
    for (t <- classes) {
      val (statics, fields) = t.fields.partition(_.isStatic)

      for (s <- statics) {
        if (!s.hasDefaultValue)
          throw new AnalysisException(s"Field $s specified as static constant but is missing a value")
        t.addConstant(new Constant(s.name, t, s.typ, s.defaultValue, s))
      }
      t.setFields(fields)
    }

    // Link default values
    for (t <- classes) {

      for (c <- t.constants)
        c.setValue(replaceUnlinkedDefaultValue(c.value, c.typ, t))

      t.setFields(t.fields.map(f => f.transform(replaceUnlinkedDefaultValue(f.defaultValue, f.typ, t))))
    }

  }

  private def replaceUnlinkedDefaultValue(d: DefaultValue, expectedType: Type, t: ClassType): DefaultValue = {
    d match {
      case d: UnlinkedDefaultValue => d.parse(expectedType, t.module)
      case d => d
    }
  }

}
