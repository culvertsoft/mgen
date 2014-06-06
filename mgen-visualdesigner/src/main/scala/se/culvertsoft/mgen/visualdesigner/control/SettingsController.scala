package se.culvertsoft.mgen.visualdesigner.control

import java.util.ArrayList

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.visualdesigner.model.Generator

class SettingsController(controller: Controller) extends SubController(controller) {

  def getGenerators() = controller.model.project.getGenerators()
  
  def setGenerators(generators: Seq[Generator]) {
    controller.model.project.setGenerators(new ArrayList(generators))
    controller.triggerObservers(_.onModelModified())
  }
 
}