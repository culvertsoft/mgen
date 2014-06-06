package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Graphics
import java.awt.Graphics2D

import javax.swing.JScrollPane
import javax.swing.JViewport
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.Entity

class ScrollableView(
  entity: Entity,
  controller: Controller,
  overlaySizePad: Int = 25)
  extends StandardView(entity, controller, overlaySizePad) {

  val scrollpane = new JScrollPane(innerPanel) {
    override def paintBorder(g: Graphics) {
      super.paintBorder(g)
      drawScrollpaneBorder(g.asInstanceOf[Graphics2D])
    }
    override def paintComponent(g: Graphics) {
      super.paintComponent(g)
      drawScrollpaneComponent(g.asInstanceOf[Graphics2D])
    }
  }

  private val savedScrollpaneWheelListener = scrollpane.getMouseWheelListeners()(0)
  scrollpane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE)
  scrollpane.getVerticalScrollBar().setUnitIncrement(10)
  scrollpane.getHorizontalScrollBar().setUnitIncrement(10)
  scrollpane.getViewport().addChangeListener(new ChangeListener() {
    override def stateChanged(e: ChangeEvent) {
      if (scrollpane.getVerticalScrollBar().isShowing() || scrollpane.getHorizontalScrollBar().isShowing()) {
        if (scrollpane.getMouseWheelListeners().isEmpty) {
          scrollpane.addMouseWheelListener(savedScrollpaneWheelListener)
        }
      } else {
        if (scrollpane.getMouseWheelListeners().nonEmpty) {
          scrollpane.removeMouseWheelListener(savedScrollpaneWheelListener)
        }
      }
    }
  })

  updateBounds()

  /**
   * ***********************************************
   *
   *
   * 		METHODS
   *
   * ***********************************************
   */

  override def minWidth(): Int = 50
  override def minHeight(): Int = 50
  override def mkBoundingComponent() = scrollpane

  /**
   * ***********************************************
   *
   *
   * 		OVERRIDEABLE DRAW CALLBACKS
   *
   * ***********************************************
   */

  protected def drawScrollpaneBorder(g: Graphics2D) {}
  protected def drawScrollpaneComponent(g: Graphics2D) {}

}