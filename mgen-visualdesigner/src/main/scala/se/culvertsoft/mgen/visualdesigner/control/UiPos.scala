package se.culvertsoft.mgen.visualdesigner.control

import java.awt.Component
import java.awt.Container
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.MouseEvent

import javax.swing.SwingUtilities

object UiPos {

   def getScreenCoordFromComp(p: Point, c: Component): Point = {
      val pc = new Point(p)
      SwingUtilities.convertPointToScreen(pc, c)
      pc
   }

   def getCompCoordFromScreen(p: Point, c: Component): Point = {
      val pc = new Point(p)
      SwingUtilities.convertPointFromScreen(pc, c)
      pc
   }

   def getCompCoordFromCompCord(p: Point, src: Component, tgt: Component): Point = {
      SwingUtilities.convertPoint(src, p.x, p.y, tgt)
   }

   def getCompBoundsFromScreen(b: Rectangle, c: Component): Rectangle = {
      val p = getCompCoordFromScreen(b.getLocation(), c)
      new Rectangle(p.x, p.y, b.width, b.height)
   }

   def getScreenBoundsFromComp(b: Rectangle, c: Component): Rectangle = {
      val p = getScreenCoordFromComp(b.getLocation(), c)
      new Rectangle(p.x, p.y, b.width, b.height)
   }

   def getCompBoundsFromCompBounds(b: Rectangle, src: Component, tgt: Component): Rectangle = {
      val p = getCompCoordFromCompCord(b.getLocation(), src, tgt)
      new Rectangle(p.x, p.y, b.width, b.height)
   }

   def getScreenBoundsOfComp(c: Component): Rectangle = {
      getScreenBoundsFromComp(c.getBounds(), c.getParent())
   }

   def getScreenPosOf(e: MouseEvent): Point = {
      if (e.getSource().asInstanceOf[Component].getParent() != null) {
         e.getLocationOnScreen()
      } else {
         getScreenCoordFromComp(e.getPoint(), e.getSource().asInstanceOf[Component])
      }
   }

}

case class UiPos(
   val onScreen: Point,
   val onComponent: Point,
   val component: Component) {

   def this(e: MouseEvent) = this(
      UiPos.getScreenPosOf(e),
      e.getPoint(),
      e.getSource().asInstanceOf[Component])

   def this(pOnComp: Point, comp: Component) = this(
      UiPos.getScreenCoordFromComp(pOnComp, comp),
      pOnComp,
      comp)

   def this() = this(new Point(), new Point(), null)

}
