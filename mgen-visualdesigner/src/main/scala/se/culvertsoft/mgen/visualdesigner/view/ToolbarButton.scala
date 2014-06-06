package se.culvertsoft.mgen.visualdesigner.view

import java.awt.event.ActionEvent

import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.ImageIcon
import se.culvertsoft.mgen.visualdesigner.images.MkImgIcon

class ToolbarButton(
   val icon: ImageIcon,
   val tooltip: String) extends AbstractAction("", icon) {
   var action: () => Unit = null

   def this(imagePath: String, tooltip: String) = this(MkImgIcon.large(imagePath), tooltip)

   if (tooltip != null && tooltip.nonEmpty)
      putValue(Action.SHORT_DESCRIPTION, tooltip)

   def setName(name: String): ToolbarButton = {
      putValue(Action.NAME, name)
      this
   }

   def setAction(f: => Unit): ToolbarButton = {
      action = () => f
      this
   }

   override def actionPerformed(event: ActionEvent) {
      if (action != null) {
         action()
      } else {
         println(s"$this.actionPerformed(..) called on event: $event")
      }
   }

}