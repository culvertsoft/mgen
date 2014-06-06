package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Graphics
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

import javax.swing.ButtonModel
import javax.swing.JButton
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import se.culvertsoft.mgen.visualdesigner.control.Controller

class FocusHelper(
   parent: AbstractView,
   controller: Controller) extends JButton {

   var _pressed = false

   override def paintBorder(g: Graphics) {
   }

   override def paintComponent(g: Graphics) {
   }

   addFocusListener(new FocusAdapter() {
      override def focusGained(e: FocusEvent) {
         controller.focusGained(parent.entity)
      }
      override def focusLost(e: FocusEvent) {
         controller.focusLost(parent.entity)
      }
   })

   getModel().addChangeListener(new ChangeListener() {
      override def stateChanged(e: ChangeEvent) {
         val model = e.getSource().asInstanceOf[ButtonModel]
         if (model.isPressed() && !_pressed) {
            controller.select(
               parent.entity,
               !controller.keyboardInputMgr.isControlDown())
         }
         _pressed = model.isPressed()
      }
   })

   setBounds(0, 0, 0, 0)
   setOpaque(false)

}
