package se.culvertsoft.mgen.visualdesigner.control

import java.awt.event.KeyEvent

class KeyboardInputController(controller: Controller) extends SubController(controller) {

   private var _isControlDown: Boolean = false

   def isControlDown(): Boolean = {
      _isControlDown
   }

   def filterGlobalEvent(e: KeyEvent): Boolean = {
      _isControlDown = e.isControlDown()
      e.getKeyCode() match {
         case KeyEvent.VK_ESCAPE => controller.mouseInputMgr.cancelMouseOperations()
         case _ =>
      }
      true
   }

}