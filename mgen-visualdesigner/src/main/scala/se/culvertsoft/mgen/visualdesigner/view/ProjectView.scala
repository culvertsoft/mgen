package se.culvertsoft.mgen.visualdesigner.view

import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.Project
import java.awt.Graphics2D
import java.awt.Color

class ProjectView(project: Project, controller: Controller) extends ScrollableView(project, controller)
  with Selectable
  with SelectionBoxable {

  override def backgroundColor(): Color = {
    Color.white
  }

  override protected def drawInnerPanelComponent(g: Graphics2D) {
    drawBackground(g)
  }

  override def add(child: AbstractView) {
    if (!child.isInstanceOf[ProjectView])
      super.add(child)
  }

}
