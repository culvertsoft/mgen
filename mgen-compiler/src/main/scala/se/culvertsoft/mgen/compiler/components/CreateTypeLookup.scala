package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import se.culvertsoft.mgen.api.model.ItemLookup
import se.culvertsoft.mgen.api.model.Project

object CreateTypeLookup {

  def apply(project: Project): ItemLookup = {

    val modules = project.allModulesRecursively

    val classes = modules.flatMap(_.classes)
    val enums = modules.flatMap(_.enums)

    val userDefined = classes ++ enums
    val constants = classes.flatMap(_.constants)

    new ItemLookup(userDefined, constants)

  }

}