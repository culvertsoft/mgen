package se.culvertsoft.mgen.visualdesigner.control

abstract class SubController(controller: Controller) {
   def selectedEntities() = controller.selectedEntities()
}