package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashSet

import javax.swing.JComponent
import javax.swing.JPanel
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.control.ModelChangeListener
import se.culvertsoft.mgen.visualdesigner.control.UiPos
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity

abstract class AbstractView(
  _entity: Entity,
  _controller: Controller,
  _overlaySizePad: Int) extends ModelChangeListener {

  lazy val boundingComponent = addBoundsChangeListener(mkBoundingComponent())

  lazy val _innerPanel = createInnerPanel()
  lazy val overlayPanel = createOverlayGlassPane()
  private val overlays = new ArrayBuffer[Overlay]

  private val childViews = new HashSet[AbstractView]

  private var _scaleFactorFromParent: Double = 1.0
  private var _viewComplexity: ViewComplexity = VIEW_COMPLEXITY_UNSET

  /**
   * **********************************************************
   *
   *             		METHODS
   *
   * *********************************************************
   */

  def unregister() { _controller.removeObserver(this) }
  def register() { _controller.addObserver(this) }

  def overlaySizePad() = _overlaySizePad
  def maxBounds() = overlayPanel.getBounds().union(bounds())
  def innerPanel(): Container = _innerPanel
  def bounds() = boundingComponent.getBounds()
  def screenBounds() = UiPos.getScreenBoundsOfComp(boundingComponent)
  def x() = bounds.x
  def y() = bounds.y
  def pos(): Point = new Point(x, y)
  def width() = bounds.width
  def height() = bounds.height
  def size() = new Dimension(width, height)
  def halfSize() = new Dimension(width / 2, height / 2)
  def top(): Int = y
  def right(): Int = x + width
  def left(): Int = x
  def bottom(): Int = y + height
  def midX(): Int = (left + right) / 2
  def midY(): Int = (top + bottom) / 2
  def midPt(): Point = new Point(midX, midY)
  def minWidth() = 10
  def minHeight() = 10
  def preferredWidth() = innerPanel.getWidth()
  def preferredHeight() = innerPanel.getHeight()
  def overlaySize(): Dimension = { new Dimension(overlayPanel.getWidth(), overlayPanel.getHeight()) }
  def overlayOffset(): Point = { new Point(overlayPanel.getX() - x, overlayPanel.getY() - y) }
  def controller(): Controller = _controller
  def entity(): Entity = _entity
  def isRootView(): Boolean = controller.viewMgr.isRoot(entity)

  def getParent(): Option[AbstractView] = {
    controller.model.parentOf(entity).map(controller.viewMgr.view)
  }

  def setX(x: Int) { setPos(x, y) }
  def setY(y: Int) { setPos(x, y) }
  def setPos(pt: Point) { setPos(pt.x, pt.y) }
  def setPos(x: Int, y: Int) { setBounds(x, y, width, height) }
  def setWidth(w: Int) { setSize(w, height) }
  def setHeight(h: Int) { setSize(width, h) }
  def setSize(dim: Dimension) { setSize(dim.width, dim.height) }
  def setSize(w: Int, h: Int) { setBounds(x, y, w, h) }
  def setBounds(bounds: Rectangle) { setBounds(bounds.x, bounds.y, bounds.width, bounds.height) }

  private def calcChildScaleFactor(): Double = {
    _scaleFactorFromParent * (if (isRoot) 1.0 else childrenFitScaleFactor)
  }

  def setBounds(x: Int, y: Int, width: Int, height: Int) {
    if (boundingComponent.getX() != x ||
      boundingComponent.getY() != y ||
      boundingComponent.getWidth() != width ||
      boundingComponent.getHeight() != height) {

      boundingComponent.setBounds(x, y, width, height)
      overlayPanel.setBounds(
        x - overlaySizePad,
        y - overlaySizePad,
        width + overlaySizePad * 2,
        height + overlaySizePad * 2)
      overlays.foreach(_.reposition())
      checkViewComplexity()
    }
  }

  def add(view: AbstractView) {
    view.beAddedToSwingParent(innerPanel)
    childViews += view
    view.updateScaleFactorReduction(calcChildScaleFactor)
  }

  def remove(view: AbstractView) {
    view.beRemovedFromSwingParent(innerPanel)
    childViews.remove(view)
  }

  def removeAllChildren() {
    for (v <- childViews.toArray) {
      remove(v)
    }
  }

  def createInnerPanel(): Container = {
    new JPanel(new AbsoluteLayout) {
      override def paintBorder(g: Graphics) {
        super.paintBorder(g)
        drawInnerPanelBorder(g.asInstanceOf[Graphics2D])
      }
      override def paintComponent(g: Graphics) {
        super.paintComponent(g)
        drawInnerPanelComponent(g.asInstanceOf[Graphics2D])
      }
      override def paintChildren(g: Graphics) {
        super.paintChildren(g)
        drawInnerPanelComponentAfterChildren(g.asInstanceOf[Graphics2D])
      }
      override def isOptimizedDrawingEnabled(): Boolean = {
        false
      }
    }
  }

  def createOverlayGlassPane(): Container = {
    new JPanel(new AbsoluteLayout) {
      setOpaque(false)
      override def paintBorder(g: Graphics) { drawOverlayPanelBorder(g.asInstanceOf[Graphics2D]) }
      override def paintComponent(g: Graphics) { drawOverlayPanelComponent(g.asInstanceOf[Graphics2D]) }
    }
  }

  def addOverlay(o: Overlay) {
    overlays += o
    overlayPanel.add(o.component(), 0)
    o.reposition()
  }

  def addOverlay(component: JComponent, x: => Int, y: => Int, w: => Int, h: => Int) {
    addOverlay(new Overlay(component, new Rectangle(x, y, w, h)))
  }

  def addOverlayOffsetBounds(component: JComponent, x: => Int, y: => Int, w: => Int, h: => Int) {
    addOverlay(new Overlay(component, new Rectangle(x - overlayOffset().x, y - overlayOffset().y, w, h)))
  }

  def validate() {
    if (!controller.isBulkOperationActive) {
      boundingComponent.validate()
      overlayPanel.validate()
    }
  }

  def isRoot(): Boolean = {
    controller.viewMgr.isRoot(entity)
  }

  def repaint() {
    if (!controller.isBulkOperationActive) {
      if (isRoot) {
        controller.viewMgr.repaintAll()
      } else {
        overlayPanel.repaint()
      }
    }
  }

  def requestFocus() {
  }

  def hasFocus(): Boolean = {
    false
  }

  def parentComponent(): Container = {
    boundingComponent.getParent()
  }

  def mkBoundingComponent(): Component = {
    innerPanel()
  }

  def updateBounds() {
    entity match {
      case e: PlacedEntity =>
        setBounds(
          (e.getPlacement().getX() * scaleFactor).toInt,
          (e.getPlacement().getY() * scaleFactor).toInt,
          (e.getPlacement().getWidth() * scaleFactor).toInt,
          (e.getPlacement().getHeight() * scaleFactor).toInt)
      case _ =>
    }
    checkViewComplexity()
  }

  final def childrenFitScaleFactor(): Double = {
    0.5
  }

  def updateScaleFactorReduction(newThisFactor: Double) {
    _scaleFactorFromParent = newThisFactor
    updateBounds()
    rescaleChildren()
  }

  def rescaleChildren() {
    val newChildFactor = calcChildScaleFactor
    for (v <- childViews)
      v.updateScaleFactorReduction(newChildFactor)
  }

  def scaleFactor(): Double = {
    math.max(0.1, controller.viewMgr.scaleFactor * _scaleFactorFromParent)
  }

  override def onIconOverrideChanged() {
    checkViewComplexity()
  }

  def checkViewComplexity() {
    val desired = desiredViewComplexity
    if (_viewComplexity != desired) {
      onViewComplexityChange(desired)
      _viewComplexity = desired
    }
  }

  def resetViewComplexity() {
    _viewComplexity = VIEW_COMPLEXITY_UNSET
    checkViewComplexity()
  }

  /**
   * **********************************************************
   *
   *             OPTIONAL OVERLOADS
   *
   * *********************************************************
   */

  protected def drawInnerPanelBorder(g: Graphics2D) {}
  protected def drawInnerPanelComponent(g: Graphics2D) {}
  protected def drawInnerPanelComponentAfterChildren(g: Graphics2D) {}
  protected def drawOverlayPanelBorder(g: Graphics2D) {}
  protected def drawOverlayPanelComponent(g: Graphics2D) {}
  protected def desiredViewComplexity(): ViewComplexity = { VIEW_COMPLEXITY_UNSET }
  protected def onViewComplexityChange(complexity: ViewComplexity) {}

  /**
   * **********************************************************
   *
   *             MANDATORY OVERLOADS
   *
   * *********************************************************
   */

  def beAddedToSwingParent(parent: Container)
  def beRemovedFromSwingParent(parent: Container)

  /**
   * **********************************************************
   *
   *             some helpers...
   *
   * *********************************************************
   */

  private final def addBoundsChangeListener(comp: Component): Component = {
    comp.addComponentListener(new ComponentAdapter {
      override def componentResized(e: ComponentEvent) { setBounds(x, y, width, height) }
      override def componentMoved(e: ComponentEvent) { setBounds(x, y, width, height) }
    })
    comp
  }

}
