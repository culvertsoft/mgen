package se.culvertsoft.mgen.visualdesigner.model

import java.awt.Point
import java.awt.Rectangle

object RichPlacedEntity {
   implicit class RichPlacedEnt(base: PlacedEntity) {
      def x(): Int = base.getPlacement().getX()
      def y(): Int = base.getPlacement().getY()
      def width(): Int = base.getPlacement().getWidth()
      def height(): Int = base.getPlacement().getHeight()
      def top(): Int = y
      def bottom(): Int = y + height
      def left(): Int = x
      def right(): Int = x + width
      def setPos(x: Int, y: Int) { base.getPlacementMutable().setX(x).setY(y) }
      def setSize(w: Int, h: Int) { base.getPlacementMutable().setWidth(w).setHeight(h) }
      def setPos(x: Double, y: Double) { setPos(x.toInt, y.toInt) }
      def setSize(w: Double, h: Double) { setSize(w.toInt, h.toInt) }
      def midX() = (left + right) / 2
      def midY() = (top + bottom) / 2
      def midPt() = new Point(midX, midY)
      def bounds() = new Rectangle(x, y, width, height)
   }
}
