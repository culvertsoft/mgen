package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList

import se.culvertsoft.mgen.api.model.Project

object RemoveParkedFields {

  def apply(project: Project) {
    val allModules = project.allModulesRecursively
    val classes = allModules.flatMap(_.classes())
    classes.foreach(c => c.setFields(c.fields().filterNot(_.isParked())))
  }

}