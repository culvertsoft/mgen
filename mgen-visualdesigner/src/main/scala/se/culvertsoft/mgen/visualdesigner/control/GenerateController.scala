package se.culvertsoft.mgen.visualdesigner.control

import se.culvertsoft.mgen.visualdesigner.model.ModelConversion

class GenerateController(controller: Controller) extends SubController(controller) {
  def generate() {
    println("Called 'generate()'")

    val apiModel = ModelConversion.vd2Api(controller.model)

    val modelBack = ModelConversion.api2Vd(apiModel)

    controller.setModel(modelBack, false, false)

  }
}