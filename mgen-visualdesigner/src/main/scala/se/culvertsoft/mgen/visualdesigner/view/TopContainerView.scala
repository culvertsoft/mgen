package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Container
import java.awt.Graphics2D

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.Project

private case class AbsolutePos(val x: Int, val y: Int)
private case class TypeWithSuper(val subType: CustomType, val superType: CustomType)

class TopContainerView(
  controller: Controller,
  topContainer: ContentPane) extends AbstractView(null, controller, 0) {

  private val absPositions = new HashMap[EntityIdBase, AbsolutePos]
  private val typesWSuper = new ArrayBuffer[TypeWithSuper]

  private var _currentChild: AbstractView = null

  topContainer.addObserver(new PaintListener() {
    override def postPaint(g: Graphics2D) {
      paintGlobalOverlay(g)
    }
  })

  /**
   * ***********************************
   *
   *
   * 				METHODS
   *
   * **********************************
   */

  override def mkBoundingComponent() = {
    topContainer
  }

  override def add(e: AbstractView) {
    if (!(e eq _currentChild)) {
      _currentChild = e
      topContainer.removeAll()
      e.beAddedToSwingParent(topContainer)
    }
  }

  override def createInnerPanel() = {
    topContainer
  }

  override def innerPanel(): Container = {
    topContainer
  }

  override def validate() {
    if (!controller.isBulkOperationActive) {
      topContainer.validate()
    }
  }

  override def repaint() {
    if (!controller.isBulkOperationActive) {
      topContainer.repaint()
    }
  }

  def currentChild(): AbstractView = {
    _currentChild
  }

  def beAddedToSwingParent(parent: Container) {
    throw new RuntimeException("Should not be here")
  }

  def beRemovedFromSwingParent(parent: Container) {
    throw new RuntimeException("Should not be here")
  }

  def clear() {
    topContainer.removeAll()
  }

  override def onViewRootChanged() {
    if (!controller.isBulkOperationActive) {
      updateScaleFactorReduction(1.0)
      validate()
      repaint()
    }
  }

  override def updateScaleFactorReduction(newThisFactor: Double) {
    _currentChild.updateScaleFactorReduction(newThisFactor)
  }

  def paintGlobalOverlay(g: Graphics2D) {

    return

    // TODO: Come back later and draw inheritance arrows

    updateLkup()

    for (TypeWithSuper(subType, superType) <- typesWSuper) {
      val pAbsSubType = absPositions(subType.getId())
      val pAbsSuperType = absPositions(superType.getId())
      drawInheritance(g, subType, superType, pAbsSubType, pAbsSuperType)
    }

  }

  def updateLkup() {

    absPositions.clear()
    typesWSuper.clear()

    controller.viewMgr.root match {
      case p: Project => updateLkup(p, 0, 0)
      case m: Module => updateLkup(m, 0, 0)
      case c: CustomType => updateLkup(c, 0, 0)
    }

  }

  def updateLkup(p: Project, _xOffs: Int, _yOffs: Int) {

    val x = _xOffs
    val y = _yOffs

    absPositions.put(p.getId(), AbsolutePos(x, y))

    for (m <- p.getModules())
      updateLkup(m, x, y)

  }

  def updateLkup(m: Module, _xOffs: Int, _yOffs: Int) {

    val x = _xOffs + m.getPlacement().getX()
    val y = _yOffs + m.getPlacement().getY()

    absPositions.put(m.getId(), AbsolutePos(x, y))

    for (p <- m.getSubmodules())
      updateLkup(p, x, y)
    for (t <- m.getTypes())
      updateLkup(t, x, y)

  }

  def updateLkup(t: CustomType, _xOffs: Int, _yOffs: Int) {

    val x = _xOffs + t.getPlacement().getX()
    val y = _yOffs + t.getPlacement().getY()

    absPositions.put(t.getId(), AbsolutePos(x, y))

    if (t.hasSuperType()) {
      controller.model.getEntity(t.getSuperType()) foreach { superType =>
        typesWSuper += TypeWithSuper(t, superType.asInstanceOf[CustomType])
      }
    }

  }

  def drawInheritance(
    g: Graphics2D,
    subType: CustomType,
    superType: CustomType,
    pAbsSubType: AbsolutePos,
    pAbsSuperType: AbsolutePos) {

    println(s"Drawing inheritance: ${subType.getName()} --|> ${superType.getName()}")
    println(s"  ${pAbsSubType} to ${pAbsSuperType}")
  }

}
