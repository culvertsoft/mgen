package se.culvertsoft.mgen.compiler.components

import scala.Option.option2Iterable
import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.UserDefinedType

class TypeLookup(
  val project: Project) {
  val modules = project.allModulesRecursively

  val classes = modules.flatMap(_.classes)
  val enums = modules.flatMap(_.enums)
  val userDefined = classes ++ enums

  val lkupFullName = userDefined.groupBy(_.fullName)
  val lkupShortName = userDefined.groupBy(_.shortName)

  def find(name: String): Iterable[UserDefinedType] = {
    lkupFullName.get(name) match {
      case Some(x) => x
      case None => lkupShortName.get(name) match {
        case Some(x) => x
        case _ => None
      }
    }
  }

}