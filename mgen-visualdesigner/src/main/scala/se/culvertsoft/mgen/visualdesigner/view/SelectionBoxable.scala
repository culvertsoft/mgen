package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import se.culvertsoft.mgen.visualdesigner.control.MouseSelectionBoxAction
import se.culvertsoft.mgen.visualdesigner.control.UiPos
import se.culvertsoft.mgen.visualdesigner.util.RichMouseEvent.RichMouseEventOps

object SelectionBoxable {
  val BOX_COLOR = Color.DARK_GRAY
}

trait SelectionBoxable
  extends AbstractView
  with BackGrounded {
  import Graphics2DOps._

  override def drawInnerPanelComponentAfterChildren(g: Graphics2D) {
    super.drawInnerPanelComponentAfterChildren(g)

    controller.mouseInputMgr.mouseDragAction() match {
      case action: MouseSelectionBoxAction if (action.containerEntity eq entity) =>

        val pOld = action.initPos.onComponent
        val pNew = UiPos.getCompCoordFromScreen(controller.mouseInputMgr.mousePos().onScreen, action.initPos.component)

        val x0 = math.min(pOld.x, pNew.x)
        val y0 = math.min(pOld.y, pNew.y)

        val x1 = math.max(pOld.x, pNew.x)
        val y1 = math.max(pOld.y, pNew.y)

        val w = x1 - x0
        val h = y1 - y0

        val selectionBox = new Rectangle(x0, y0, w, h)

        g.drawRect(x0, y0, w, h)

      case _ =>
    }
  }

  val selectionBoxableListener = new MouseAdapter {
    override def mouseDragged(e: MouseEvent) {
      if (e.isLeftBtn) {
        controller.mouseInputMgr.startMouseSelectionBoxAction(e, entity)
      }
    }
  }

  innerPanel.addMouseMotionListener(selectionBoxableListener)

  override def drawBackground(g: Graphics2D) {
    super.drawBackground(g)
  }

}
