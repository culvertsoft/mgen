package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Graphics2D
import scala.collection.JavaConversions.asScalaBuffer
import Graphics2DOps.RichGraphics2D
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicArrowButton
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import java.awt.Dimension
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity

object ModuleView {
  val BG_COLOR = new Color(240, 240, 240)
  val grey_arrow = new BasicArrowButton(0)
  // val blue_arrow = new BasicArrowButton(0, Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)
}

class ModuleView(module: Module, controller: Controller)
  extends ScrollableView(module, controller)
  with BackGrounded
  with Bordered
  with Labeled
  with Selectable
  with SelectionBoxable
  with Movable
  with Resizeable
  with CursorChanging {
  import ModuleView._
  import Graphics2DOps._

  override def labelText(): String = module.getName()
  override def resizeableAtCorners(): Boolean = true
  override def resizeableEW(): Boolean = true
  override def resizeableNS(): Boolean = true

  override protected def drawScrollpaneBorder(g: Graphics2D) { drawBorder(g) }
  override protected def drawInnerPanelComponent(g: Graphics2D) {
    drawBackground(g)
    drawInheritanceArrows(g)
  }

  def drawInheritanceArrows(g: Graphics2D) {
    for (subType <- module.getTypes()) {
      controller.model.superTypeOf(subType) match {
        case Some(superType) if (subType.getParent() == superType.getParent()) =>
          drawInheritanceArrow(subType, superType, g)
        case _ =>
      }
    }
  }

  /* 
   *  This is broken: When dragging items across parents, this canges itself...bad..
  override def childrenFitScaleFactor(): Double = {

    if (module.getSubmodules().isEmpty() && module.getTypes().isEmpty()) {
      return 1.0
    }

    // TODO: Take into account actual size of children
    val spaceAvailable = new Dimension(module.getPlacement().getWidth(), module.getPlacement().getHeight())

    def forAllChildren(f: PlacedEntity => Unit) {
      module.getSubmodules foreach f
      module.getTypes foreach f
    }

    var xMax = 1
    var yMax = 1
    forAllChildren { e =>
      val right = e.getPlacement().getX() + e.getPlacement().getWidth()
      val bottom = e.getPlacement().getY() + e.getPlacement().getHeight()
      xMax = math.max(xMax, right)
      yMax = math.max(yMax, bottom)
    }

    val s = 1.0 / math.max(
      xMax.toDouble / spaceAvailable.width.toDouble,
      yMax.toDouble / spaceAvailable.height.toDouble)

    return math.max(0.50, math.min(s, 1.0))
  }*/

  def drawInheritanceArrow(subType: CustomType, superType: CustomType, g: Graphics2D) {

    val subTypeView = controller.viewMgr.getView(subType).asInstanceOf[AbstractView with Selectable]
    val superTypeView = controller.viewMgr.getView(superType).asInstanceOf[AbstractView with Selectable]

    import se.culvertsoft.mgen.visualdesigner.util.Asof._

    val selected = subTypeView.isSelected || superTypeView.isSelected

    val scale = scaleFactor
    val arrowWidth = math.max(5, (10 * scale).toInt)
    val minLineHeight = (10 * scale).toInt
    val x0 = subTypeView.midX
    val y0 = subTypeView.y - labelPanel.getHeight()
    val xLast = superTypeView.midX
    val yLast = superTypeView.bottom + arrowWidth

    g.color(if (selected) Color.BLUE else Color.DARK_GRAY) {
      g.lineWidth(if (selected) 2 else 1) {

        val xStart = x0
        val yStart = y0 - minLineHeight
        val xEnd = xLast
        val yEnd = yLast + minLineHeight

        val (xHalfWayA, yHalfWayA, xHalfWayB, yHalfWayB) =
          if (yStart >= yEnd) {
            (xStart, (yStart + yEnd) / 2, xEnd, (yStart + yEnd) / 2)
          } else {
            ((xStart + xEnd) / 2, yStart, (xStart + xEnd) / 2, yEnd)
          }

        { // Draw lines
          g.drawLine(x0, y0, xStart, yStart)
          g.drawLine(xStart, yStart, xHalfWayA, yHalfWayA)
          g.drawLine(xHalfWayA, yHalfWayA, xHalfWayB, yHalfWayB)
          g.drawLine(xHalfWayB, yHalfWayB, xEnd, yEnd)
          g.drawLine(xEnd, yEnd, xLast, yLast)
        }

        { // Draw arrow head
          val x = superTypeView.midX - arrowWidth / 2 + 1
          val y = superTypeView.bottom
          // val arrow = if (selected) blue_arrow else grey_arrow
          grey_arrow.paintTriangle(g, x, y, arrowWidth, SwingConstants.NORTH, true)
        }
      }
    }
  }

}
