package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Constant
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.ItemLookup
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

  private def replace(t: Type, referencedFrom: ClassType)(implicit lkup: ItemLookup): Type = {

    t match {
      case t: ArrayType =>
        new ArrayType(replace(t.elementType, referencedFrom))
      case t: ListType =>
        new ListType(replace(t.elementType, referencedFrom))
      case t: MapType =>
        new MapType(replace(t.keyType, referencedFrom), replace(t.valueType, referencedFrom))
      case t: UnlinkedType =>
        lkup.getType(t.name(), referencedFrom)
      case null => throw new AnalysisException("Cannot provide null type to type")

      case _ => t

    }

  }

  private def link(project: Project) {

    implicit val typeLkup = CreateTypeLookup(project)
    val classes = typeLkup.getClasses

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

  private def replaceUnlinkedDefaultValue(d: DefaultValue, expectedType: Type, t: ClassType)(implicit typeLkup: ItemLookup): DefaultValue = {
    d match {
      case d: UnlinkedDefaultValue => d.parse(expectedType, t, typeLkup)
      case d => d
    }
  }

}
