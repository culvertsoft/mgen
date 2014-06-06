package se.culvertsoft.mgen.visualdesigner.util

import java.awt.event.InputEvent
import java.awt.event.MouseEvent

import javax.swing.SwingUtilities

object RichMouseEvent {

  val BACK_BTN_MASK = InputEvent.getMaskForButton(4)
  val FWD_BTN_MASK = InputEvent.getMaskForButton(5)

  implicit class RichMouseEventOps(base: MouseEvent) {

    def button(): MouseButton = {
      
      if (SwingUtilities.isLeftMouseButton(base)) {
        LeftMouseButton
      } else if (SwingUtilities.isMiddleMouseButton(base)) {
        MiddleMouseButton
      } else if (SwingUtilities.isRightMouseButton(base)) {
        RightMouseButton
      } else if ((base.getModifiersEx() & BACK_BTN_MASK) == BACK_BTN_MASK) {
        BackMouseButton
      } else if ((base.getModifiersEx() & FWD_BTN_MASK) == FWD_BTN_MASK) {
        ForwardMouseButton
      } else {
        UnknownMouseButton
      }
    }

    def isLeftBtn(): Boolean = isBtn(LeftMouseButton)
    def isMiddleBtn(): Boolean = isBtn(MiddleMouseButton)
    def isRightBtn(): Boolean = isBtn(RightMouseButton)
    def isBackBtn(): Boolean = isBtn(BackMouseButton)
    def isForwardBtn(): Boolean = isBtn(ForwardMouseButton)
    def isBtn(reference: MouseButton): Boolean = RichMouseEvent.isBtn(button, reference)

    def isPress(): Boolean = isTyp(MousePress)
    def isRelease(): Boolean = isTyp(MouseRelease)
    def isClick(): Boolean = isTyp(MouseClick)
    def isExit(): Boolean = isTyp(MouseExit)
    def isEnter(): Boolean = isTyp(MouseEnter)
    def isMove(): Boolean = isTyp(MouseMove)
    def isDrag(): Boolean = isTyp(MouseDrag)
    def isTyp(reference: MouseEventType): Boolean = RichMouseEvent.isTyp(typ, reference)

    def typ(): MouseEventType = {
      base.getID() match {
        case MouseEvent.MOUSE_PRESSED => MousePress
        case MouseEvent.MOUSE_RELEASED => MouseRelease
        case MouseEvent.MOUSE_CLICKED => MouseClick
        case MouseEvent.MOUSE_EXITED => MouseExit
        case MouseEvent.MOUSE_ENTERED => MouseEnter
        case MouseEvent.MOUSE_MOVED => MouseMove
        case MouseEvent.MOUSE_DRAGGED => MouseDrag
        case _ => MouseUnknownEventType
      }
    }

  }

  def isBtn(btn: MouseButton, reference: MouseButton): Boolean = {
    btn == reference
  }

  def isTyp(typ: MouseEventType, reference: MouseEventType): Boolean = {
    typ == reference
  }

}

abstract class MouseEventType {

}

case object MouseMove extends MouseEventType
case object MouseDrag extends MouseEventType
case object MousePress extends MouseEventType
case object MouseRelease extends MouseEventType
case object MouseClick extends MouseEventType
case object MouseExit extends MouseEventType
case object MouseEnter extends MouseEventType
case object MouseUnknownEventType extends MouseEventType

case object MouseMoveEvent extends MouseEventType

abstract class MouseButton {
  def isLeft() = RichMouseEvent.isBtn(this, LeftMouseButton)
  def isMiddle() = RichMouseEvent.isBtn(this, MiddleMouseButton)
  def isRight() = RichMouseEvent.isBtn(this, RightMouseButton)
  def isUnknown() = RichMouseEvent.isBtn(this, UnknownMouseButton)
  def isBack() = RichMouseEvent.isBtn(this, BackMouseButton)
  def isForward() = RichMouseEvent.isBtn(this, ForwardMouseButton)
}

case object LeftMouseButton extends MouseButton
case object MiddleMouseButton extends MouseButton
case object RightMouseButton extends MouseButton
case object BackMouseButton extends MouseButton
case object ForwardMouseButton extends MouseButton
case object UnknownMouseButton extends MouseButton
