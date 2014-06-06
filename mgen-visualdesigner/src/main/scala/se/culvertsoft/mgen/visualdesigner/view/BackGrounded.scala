package se.culvertsoft.mgen.visualdesigner.view

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D

import Graphics2DOps.RichGraphics2D

object BackGrounded {
   val BG_COLOR = new Color(240, 240, 240)
}

trait BackGrounded {
   self: AbstractView =>
   import Graphics2DOps._
   import BackGrounded._

   val dashedLine = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, Array(1.0f), 0);

   def backgroundColor(): Color = {
      BG_COLOR
   }

   def drawBackground(g: Graphics2D) {

      g.color(backgroundColor()) {
         g.fillRect(-10, -10, preferredWidth() + 15, preferredHeight() + 15)
      }

      if (hasFocus()) {
         g.stroke(dashedLine) {
            val focusLineInset = 2
            g.drawRect(
               focusLineInset,
               focusLineInset,
               preferredWidth() - 2 * focusLineInset - 1,
               preferredHeight() - 2 * focusLineInset - 1)
         }
      }
   }

}