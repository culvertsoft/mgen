package se.culvertsoft.mgen.visualdesigner.view.autobox2

import se.culvertsoft.mgen.visualdesigner.control.Controller

class SelectViewRootAutoBox2(controller: Controller) extends FindEntityAutoComboBox2(controller) {
  override def allowFields(): Boolean = false
  override def allowClasses(): Boolean = false
  override def allowModules(): Boolean = true
  override def allowProjects(): Boolean = true
}