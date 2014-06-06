package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import Graphics2DOps.RichGraphics2D
import javax.swing.JPanel

object Inheritable {
   val INHERITS_COLOR = Color.CYAN
}

trait Inheritable {
   self: ClassView with Selectable =>
   import Graphics2DOps._
   import Inheritable._

   private val D = Resizeable.RESIZE_SQUARE_WIDTH * 6 / 5
   private val r = D / 2

   val inhUpPanel = new InhBall() { setToolTipText("Click and drag from this point to set a super class") }
   val inhDownPanel = new InhBall() { setToolTipText("Click and drag from this point to create a new sub class") }

   inhUpPanel.addMouseListener(selectMouseListenerWOFocus)
   inhDownPanel.addMouseListener(selectMouseListenerWOFocus)

   inhUpPanel.addMouseMotionListener(new MouseAdapter() {
      override def mouseDragged(e: MouseEvent) {
         controller.mouseInputMgr.startMouseDragAttachSuperTypeAction(entity, e)
      }
   })

   inhDownPanel.addMouseMotionListener(new MouseAdapter() {
      override def mouseDragged(e: MouseEvent) {
         controller.mouseInputMgr.startMouseDragCreateSubTypeAction(entity, e)
      }
   })

   addOverlayOffsetBounds(inhUpPanel, width() / 2 - r, -r, D, D)
   addOverlayOffsetBounds(inhDownPanel, width() / 2 - r, height() - r, D, D)

   def drawInheritable(g: Graphics2D) {

      def fillCall = (g.fillArc _).tupled
      def drawCall = (g.drawArc _).tupled

      val args = (0, 0, D, D, 0, 360)

      if (isHovered() || isSelected()) {
         g.color(borderColor()) {
            fillCall(args)
         }
      }

   }

   class InhBall extends JPanel {
      override def paintBorder(g: Graphics) {}
      override def paintComponent(g: Graphics) {
         drawInheritable(g.asInstanceOf[Graphics2D])
      }
      setSize(D, D)
   }

}