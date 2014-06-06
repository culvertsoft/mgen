package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Graphics
import java.awt.Graphics2D

import javax.swing.JPanel
import se.culvertsoft.mgen.visualdesigner.util.Observable

class ContentPane
   extends JPanel
   with Observable[PaintListener] {

   override def paintChildren(g: Graphics) {
      triggerObservers(_.prePaint(g.asInstanceOf[Graphics2D]))
      super.paintChildren(g)
      triggerObservers(_.postPaint(g.asInstanceOf[Graphics2D]))
   }
}
