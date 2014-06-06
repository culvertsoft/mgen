package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import se.culvertsoft.mgen.visualdesigner.util.Asof.AsofOps
import se.culvertsoft.mgen.visualdesigner.util.RichMouseEvent.RichMouseEventOps
object Selectable {
  val DEFAULT_SELECTED_BORDER_COLOR = Color.BLUE
  val DEFAULT_HOVERED_BORDER_COLOR = new Color(175, 175, 245);
}

trait Selectable extends Bordered {
  self: AbstractView =>
  import Graphics2DOps._
  import Selectable._

  private var _selected = false
  private var _hovered = false

  private def mkSelectMouseListener(reFocus: Boolean) = new MouseAdapter {
    override def mousePressed(e: MouseEvent) {
      if (e.isLeftBtn) {
        if (e.isControlDown()) {
          if (isSelected()) {
            controller.deSelect(entity, false)
          } else {
            controller.select(entity, false, reFocus)
          }
        } else {
          controller.select(entity, !isSelected(), reFocus)
        }
      }
    }
    override def mouseClicked(e: MouseEvent) {
      if (e.isLeftBtn) {
        if (!e.isControlDown()) {
          if (isSelected()) {
            controller.select(entity, true, reFocus)
          }
        }
      }
    }
    override def mouseEntered(e: MouseEvent) {
      controller.hover(true, Selectable.this, entity)
    }
    override def mouseExited(e: MouseEvent) {
      controller.hover(false, Selectable.this, entity)
    }
  }

  val selectMouseListenerWFocus = mkSelectMouseListener(true)
  val selectMouseListenerWOFocus = mkSelectMouseListener(false)

  innerPanel.addMouseListener(selectMouseListenerWFocus)
  this.ifIs[Labeled](_.labelPanel.addMouseListener(selectMouseListenerWFocus))

  override def borderColor(): Color = {
    this match {
      case _ if (isSelected()) => DEFAULT_SELECTED_BORDER_COLOR
      case _ if (isHovered()) => DEFAULT_HOVERED_BORDER_COLOR
      case _ => super.borderColor()
    }
  }

  def isHovered(): Boolean = {
    _hovered
  }

  def isSelected(): Boolean = {
    _selected
  }

  def setHovered(state: Boolean) {
    if (state != _hovered) {
      _hovered = state
      if (!controller.isBulkOperationActive) {
        self.repaint()
      }
    }
  }

  def setSelected(state: Boolean) {
    if (_selected != state) {
      _selected = state
    }
  }

}
