package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Cursor
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import se.culvertsoft.mgen.visualdesigner.util.Asof.AsofOps
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity

trait Movable {
  self: AbstractView =>
  import Graphics2DOps._
  import Labeled._

  val moveByMouseListener = new MouseAdapter() {
    override def mouseEntered(e: MouseEvent) { controller.mouseInputMgr.setCursor(Cursor.MOVE_CURSOR) }
    override def mouseExited(e: MouseEvent) { controller.mouseInputMgr.setCursor(Cursor.DEFAULT_CURSOR) }
    override def mousePressed(e: MouseEvent) {
      if (!e.isControlDown()) {
        entity match {
          case entity: PlacedEntity => controller.mouseInputMgr.startMouseMoveAction(entity)
          case _ =>
        }

      }
    }
  }

  this.ifIs[Labeled](l => {
    l.labelPanel.addMouseListener(moveByMouseListener)
  })

}
