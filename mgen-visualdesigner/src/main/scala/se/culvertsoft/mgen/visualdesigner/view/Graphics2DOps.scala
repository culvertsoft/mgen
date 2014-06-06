package se.culvertsoft.mgen.visualdesigner.view

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Stroke

object Graphics2DOps {

   implicit class RichGraphics2D(g: Graphics2D) {

      def color(c: Color)(f: => Unit) {
         val prevColor = g.getColor()
         g.setColor(c)
         f
         g.setColor(prevColor)
      }

      def stroke(s: Stroke)(f: => Unit) {
         val prevStroke = g.getStroke()
         g.setStroke(s)
         f
         g.setStroke(prevStroke)
      }

      def transl(x: Int, y: Int)(f: => Unit) {
         g.translate(x, y)
         f
         g.translate(-x, -y)
      }

      def transl(p: Point)(f: => Unit) {
         transl(p.x, p.y)(f)
      }

      def lineWidth(lw: Int)(f: => Unit) {
         stroke(new BasicStroke(lw))(f)
      }

   }

}