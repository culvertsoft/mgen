package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Graphics2D

import Graphics2DOps.RichGraphics2D
import se.culvertsoft.mgen.visualdesigner.util.Asof.AsofOps

object Bordered {
   val DEFAULT_COLOR = new Color(128, 128, 128)
}

trait Bordered {
   self: AbstractView =>
   import Graphics2DOps._
   import Bordered._

   def borderColor(): Color = {
      DEFAULT_COLOR
   }

   def drawBorder(g: Graphics2D) {
/*
      self.ifIs[AbstractView with BackGrounded] { self =>
         g.color(self.backgroundColor()) {
            g.lineWidth(5) {
               g.drawRect(0, 0, width - 1, height - 1);
            }
         }
      }
*/
      g.color(borderColor()) {
         g.drawRect(0, 0, width - 1, height - 1);
      }
   }

}