package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener

import Graphics2DOps.RichGraphics2D
import javax.swing.JPanel
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.util.Asof.AsofOps
import se.culvertsoft.mgen.visualdesigner.util.RichMouseEvent.RichMouseEventOps

object Resizeable {
   val RESIZE_SQUARE_WIDTH = 10
   val HALF_RESIZE_SQUARE_WIDTH = RESIZE_SQUARE_WIDTH / 2

   abstract class ResizePoint(
      val xSign: Double,
      val ySign: Double,
      val xMove: Double,
      val yMove: Double)

   object ResizePointN extends ResizePoint(0, -1, 0, 1)
   object ResizePointNE extends ResizePoint(1, -1, 0, 1)
   object ResizePointE extends ResizePoint(1, 0, 0, 0)
   object ResizePointSE extends ResizePoint(1, 1, 0, 0)
   object ResizePointS extends ResizePoint(0, 1, 0, 0)
   object ResizePointSW extends ResizePoint(-1, 1, 1, 0)
   object ResizePointW extends ResizePoint(-1, 0, 1, 0)
   object ResizePointNW extends ResizePoint(-1, -1, 1, 1)

}

trait Resizeable {
   self: AbstractView =>
   import Resizeable._
   import Graphics2DOps._

   def resizeableAtCorners(): Boolean = false
   def resizeableEW(): Boolean = false
   def resizeableNS(): Boolean = false
   def resizeSquareWidth(): Int = RESIZE_SQUARE_WIDTH

   val RESIZE_SQUARE_N = mkResizeSquare(ResizePointN, resizeableNS)
   val RESIZE_SQUARE_NE = mkResizeSquare(ResizePointNE, resizeableAtCorners)
   val RESIZE_SQUARE_E = mkResizeSquare(ResizePointE, resizeableEW)
   val RESIZE_SQUARE_SE = mkResizeSquare(ResizePointSE, resizeableAtCorners)
   val RESIZE_SQUARE_S = mkResizeSquare(ResizePointS, resizeableNS)
   val RESIZE_SQUARE_SW = mkResizeSquare(ResizePointSW, resizeableAtCorners)
   val RESIZE_SQUARE_W = mkResizeSquare(ResizePointW, resizeableEW)
   val RESIZE_SQUARE_NW = mkResizeSquare(ResizePointNW, resizeableAtCorners)

   def drawResizeSquare(g: Graphics2D, p: ResizePoint) {
      this match {
         case _this: Selectable =>
            if (_this.isHovered() || _this.isSelected()) {
               g.color(_this.borderColor()) {
                  val b = resizeSquareBounds(p)
                  g.fillRect(0, 0, b.width, b.height)
               }
            }
         case _ =>
      }
   }

   def resizeSquareBounds(p: ResizePoint): Rectangle = {
      val xw = -resizeSquareWidth / 2
      val xe = width() - resizeSquareWidth / 2
      val xm = (xw + xe) / 2
      val yn = -resizeSquareWidth / 2
      val ys = height() - resizeSquareWidth / 2
      val ym = (yn + ys) / 2
      p match {
         case ResizePointN => new Rectangle(xm, yn, resizeSquareWidth, resizeSquareWidth)
         case ResizePointNE => new Rectangle(xe, yn, resizeSquareWidth, resizeSquareWidth)
         case ResizePointE => new Rectangle(xe, ym, resizeSquareWidth, resizeSquareWidth)
         case ResizePointSE => new Rectangle(xe, ys, resizeSquareWidth, resizeSquareWidth)
         case ResizePointS => new Rectangle(xm, ys, resizeSquareWidth, resizeSquareWidth)
         case ResizePointSW => new Rectangle(xw, ys, resizeSquareWidth, resizeSquareWidth)
         case ResizePointW => new Rectangle(xw, ym, resizeSquareWidth, resizeSquareWidth)
         case ResizePointNW => new Rectangle(xw, yn, resizeSquareWidth, resizeSquareWidth)
      }
   }

   def resizeTo(sz: Dimension, p: ResizePoint) {

   }

   def mkResizeListener(p: ResizePoint): MouseListener = {
      new MouseAdapter() {
         override def mousePressed(e: MouseEvent) {
            if (e.isLeftBtn) {
               entity match {
                  case entity: PlacedEntity => controller.mouseInputMgr.startMouseResizeAction(e, p, entity)
                  case _ =>
               }
            }
         }
      }
   }

   private final def mkResizeSquare(p: ResizePoint, add: Boolean = true): JPanel = {

      val out = new JPanel() {

         Resizeable.this.ifIs[Selectable](s => addMouseListener(new MouseAdapter() {
            override def mousePressed(e: MouseEvent) {
               if (!e.isControlDown() && e.isLeftBtn) {
                  controller.deselectAll(null)
               }
            }
         }))
         Resizeable.this.ifIs[Selectable](s => addMouseListener(s.selectMouseListenerWOFocus))

         override def paintComponent(g: Graphics) {
            drawResizeSquare(g.asInstanceOf[Graphics2D], p)
         }

         addMouseListener(mkResizeListener(p))

      }

      if (add) {
         addOverlayOffsetBounds(out,
            resizeSquareBounds(p).x,
            resizeSquareBounds(p).y,
            resizeSquareBounds(p).width,
            resizeSquareBounds(p).height)
      }

      out
   }

}
