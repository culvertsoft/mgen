package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Cursor
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle
import java.util.IdentityHashMap
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.reflect.ClassTag
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.SwingUtilities
import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.control.ControllerListener
import se.culvertsoft.mgen.visualdesigner.control.UiPos
import se.culvertsoft.mgen.visualdesigner.control.UndoBuffer
import se.culvertsoft.mgen.visualdesigner.model.ChildParent
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EntityId
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.model.Project
import se.culvertsoft.mgen.visualdesigner.util.AwtMath.RichDimension
import se.culvertsoft.mgen.visualdesigner.util.AwtMath.RichPoint
import se.culvertsoft.mgen.visualdesigner.util.OperationStatus
import se.culvertsoft.mgen.visualdesigner.view.searchdialog.SearchDialog
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase
import se.culvertsoft.mgen.visualdesigner.control.ModelChangeListener
import se.culvertsoft.mgen.visualdesigner.Actions
import java.awt.Component

class ViewManager(
  private val controller: Controller,
  private val topContainer: ContentPane) extends ControllerListener {
  private val topView = new TopContainerView(controller, topContainer)
  private val views = new IdentityHashMap[Entity, AbstractView]
  private var _root: Entity = controller.model().project
  private var _scaleFactor = 1.0
  private var _iconOverride: Boolean = false
  private val searchDialog: SearchDialog = new SearchDialog(controller)
  private val viewHistory = new UndoBuffer[EntityIdBase](_root.getId, _ => 32)
  private val movingThroughHistory = new OperationStatus

  controller.addObserver(this)
  controller.addObserver(topView)

  /**
   * ************************************************************************
   *
   *
   * 				API
   *
   * ***********************************************************************
   */

  def rootView(): ScrollableView = {
    getView(root).asInstanceOf[ScrollableView]
  }

  def isIconOverrideActive(): Boolean = {
    _iconOverride
  }

  def toggleIconOverride() {
    setIconOverride(!isIconOverrideActive)
  }

  def setIconOverride(state: Boolean) {
    if (_iconOverride != state) {
      _iconOverride = state
      controller.triggerObservers(_.onIconOverrideChanged())
    }
  }

  def parentOf(view: AbstractView): Option[AbstractView] = {
    controller.model.parentOf(view.entity) match {
      case Some(parentEntity) => Some(getView(parentEntity))
      case _ => None
    }
  }

  def validateAll() {
    topContainer.validate()
  }

  def repaintAll() {
    topContainer.repaint()
  }

  def setCursor(c: Cursor) {
    topContainer.setCursor(c)
  }

  def showConfirmDialog(msg: String, title: String, qType: Int, diaType: Int): Int = {
    JOptionPane.showConfirmDialog(topContainer, msg, title, qType, diaType)
  }

  def showSaveDialog(fc: JFileChooser): Int = {
    fc.showSaveDialog(topContainer)
  }

  def showOpenDialog(fc: JFileChooser): Int = {
    fc.showOpenDialog(topContainer)
  }

  def getWindow(): JFrame = {
    SwingUtilities.getWindowAncestor(topContainer).asInstanceOf[JFrame]
  }

  def scaleFactor(): Double = {
    _scaleFactor
  }

  def scaleFactorIndex(): Int = {
    ZoomLevels.findClosestIndex(scaleFactor)
  }

  def root(): Entity = {
    _root
  }

  def isRoot(entity: Entity) = {
    entity eq root()
  }

  def isViewRoot(view: AbstractView) = {
    getView(root()) eq view
  }

  def getView(entity: Entity): AbstractView = {
    if (entity != null) {
      views.get(entity)
    } else {
      topView
    }
  }

  def getAllViews[T: ClassTag](): Seq[T with AbstractView] = {
    val out = new ArrayBuffer[T]
    controller.model.project.foreach { cp =>
      views.get(cp.child) match {
        case x: T => out += x
        case _ =>
      }
    }
    out.asInstanceOf[Seq[T with AbstractView]]
  }

  def getUiPosOf(entity: Entity): UiPos = {
    val view = views.get(entity)
    new UiPos(view.pos(), view.boundingComponent.getParent())
  }

  def getBoundsOf(entity: Entity): Rectangle = {
    val view = views.get(entity)
    view.bounds()
  }

  def getSizeOf(entity: Entity): Dimension = {
    val view = views.get(entity)
    view.size()
  }

  def getMidPtOf(entity: Entity): Point = {
    val view = views.get(entity)
    view.midPt()
  }

  def getScreenBoundsOf(entity: Entity): Rectangle = {
    val uiPos = getUiPosOf(entity)
    entity match {
      case project: Project => new Rectangle(uiPos.onScreen.x, uiPos.onScreen.y, topContainer.getWidth(), topContainer.getHeight())
      case _ =>
        val view = getView(entity)
        new Rectangle(uiPos.onScreen.x, uiPos.onScreen.y, view.width(), view.height())
    }
  }

  def setViewRootShallow(newRoot: Entity, triggerObservers: Boolean = true) {
    _root = newRoot
  }

  def setViewRoot(newRoot: EntityIdBase) {
    controller.model.getEntity(newRoot) foreach setViewRoot
  }

  def setViewRoot(newRoot: Entity) {

    if (!movingThroughHistory.isActive) {
      viewHistory.pushState(root.getId(), false)
      viewHistory.pushState(newRoot.getId(), false)
    }

    setViewRootShallow(newRoot, false)

    // Get the previous root 
    val prevRootView = topView.currentChild()

    if (prevRootView == null || !(newRoot eq prevRootView.entity)) {

      // Add the previous root back to its correct parent
      if (prevRootView != null) {
        for (properParent <- controller.model.parentOf(prevRootView.entity)) {
          val properParentView = getView(properParent)
          prevRootView.updateBounds()
          properParentView.add(prevRootView)
        }
      }

      topView.add(getView(root))

    }

    controller.triggerObservers(_.onViewRootChanged())

  }

  /**
   * ************************************************************************
   *
   *
   * 				CONTROLLERLISTENER IMPLEMENTATION
   *
   * ***********************************************************************
   */

  override def onSelectionChanged(selection: Seq[Entity], focused: Option[Entity]) {
    for (e <- focused) {
      val view = getView(e)
      if (!view.hasFocus()) {
        view.requestFocus()
      }
    }
    if (!controller.isBulkOperationActive) {
      topContainer.repaint()
    }
  }

  private def mkViewFor(entity: Entity) {

    if (getView(entity) != null)
      return

    println(s"created new view for ${entity.getName()}")

    val newItem = entity match {
      case entity: Project =>
        new ProjectView(entity, controller)
      case entity: Module =>
        new ModuleView(entity, controller)
      case entity: CustomType =>
        new ClassView(entity, controller)
      case entity: CustomTypeField =>
        new FieldView(entity, controller)
    }
    views.put(entity, newItem)

    controller.addObserver(newItem)

  }

  override def onEntityAdded(child: Entity, parent: Entity) {

    mkViewFor(child)

    val parentView = getView(parent)
    parentView.add(getView(child))
    if (!controller.isBulkOperationActive) {
      parentView.validate()
      parentView.repaint()
    }
  }

  override def onEntityTransferred(child: Entity, newParent: Entity, oldParent: Entity) {

    val childView = getView(child)
    val oldParentView = getView(oldParent)
    val newParentView = getView(newParent)

    val childHadFocus = childView.hasFocus()

    oldParentView.remove(childView)
    newParentView.add(childView)

    if (!(newParent eq oldParent)) {
      childView.updateBounds()
      if (!controller.isBulkOperationActive) {
        oldParentView.validate()
        oldParentView.repaint()
      }
    }

    if (!controller.isBulkOperationActive)
      newParentView.validate()

    if (childHadFocus)
      childView.requestFocus()

  }

  override def onEntityDeleted(child: Entity, parent: Option[Entity]) {

    for (parent <- parent) {
      val parentView = getView(parent)
      val childView = getView(child)

      parentView.remove(childView)
      if (!controller.isBulkOperationActive) {
        parentView.validate()
        parentView.repaint()
      }
    }

    child.foreach { cp =>
      val xView = views.remove(getView(cp.child))
      if (xView != null) {
        controller.removeObserver(xView)
      }
    }

  }

  override def onEntityMoved(e: PlacedEntity, parent: Entity) {
    getView(e).updateBounds()
    if (!controller.isBulkOperationActive) {
      getView(parent).validate()
    }
  }

  override def onEntityResized(e: PlacedEntity, parent: Entity) {
    getView(e).updateBounds()
    if (!controller.isBulkOperationActive) {
      getView(parent).validate()
    }
  }

  override def onModelModified(isPreview: Boolean) {
    if (!controller.isBulkOperationActive) {
      topView.validate()
      topView.repaint()
    }
  }

  override def onModelCleared() {
    views foreach (_._2.unregister())
    views.clear()
    topView.clear()
  }

  override def onFocusGained(entity: Entity) {
    if (!controller.isBulkOperationActive) {
      getView(entity).repaint()
    }
  }

  override def onFocusLost(entity: Entity) {
    if (!controller.isBulkOperationActive) {
      val x = getView(entity)
      if (x != null)
        x.repaint()
    }
  }

  override def onEntityModified(child: Entity, validate: Boolean = false, parent: Option[Entity] = None) {
    if (!controller.isBulkOperationActive) {
      val v = getView(child)
      if (v != null) {
        if (validate)
          v.validate()
        v.repaint()
      }
    }
  }

  override def onChildrenReordered(parent: Entity) {
    if (!controller.isBulkOperationActive) {
      val view = getView(parent)
      val children = parent.firstLevelChildren()
      val childViews = children map getView
      childViews foreach view.remove
      childViews foreach view.add
      view.validate()
    }
  }

  def popupGetString(message: String, defaultValue: String = "", triggeringComponent: Component = topContainer): Option[String] = {
    val o = JOptionPane.showInputDialog(triggeringComponent, message, defaultValue);
    if (o != null) Some(o) else None
  }

  def popupFailed(message: String, label: String): Boolean = {
    JOptionPane.showMessageDialog(topContainer, message, label, JOptionPane.INFORMATION_MESSAGE)
    false
  }

  def popupPreconditionFailed(message: String): Boolean = {
    popupFailed(message, "Precondition check failed")
  }

  def getCurrentScrollPane(): JScrollPane = {
    getView(root()).asInstanceOf[ScrollableView].scrollpane
  }

  def keepScrollbarPos(f: => Unit) {

    val scrollpaneBefore = getCurrentScrollPane()
    val hBarBefore = scrollpaneBefore.getHorizontalScrollBar()
    val vBarBefore = scrollpaneBefore.getVerticalScrollBar()

    val h = if (hBarBefore.isVisible()) Some(hBarBefore.getValue()) else None
    val v = if (vBarBefore.isVisible()) Some(vBarBefore.getValue()) else None

    f

    val scrollpaneAfter = getCurrentScrollPane()
    val hBarAfter = scrollpaneAfter.getHorizontalScrollBar()
    val vBarAfter = scrollpaneAfter.getVerticalScrollBar()

    for (h <- h) {
      if (h != hBarAfter.getValue()) {
        hBarAfter.setValue(h)
      }
    }

    for (v <- v) {
      if (v != vBarAfter.getValue()) {
        vBarAfter.setValue(v)
      }
    }

  }

  def findDeepestEntityAt(
    screenPos: Point,
    pickFilter: Entity => Boolean,
    continueFilter: Entity => Boolean): Option[Entity] = {

    var out: Option[Entity] = None

    root().traverse {
      case ChildParent(entity, _) => {
        if (isVisible(entity) && getScreenBoundsOf(entity).contains(screenPos)) {
          if (pickFilter(entity))
            out = Some(entity)
          continueFilter(entity)
        } else {
          false
        }
      }
    }

    out

  }

  def isVisible(entity: Entity): Boolean = {

    if (isRoot(entity))
      return true

    if (entity.isInstanceOf[Project])
      return false

    if (!root.contains(entity))
      return false

    controller.model.parentOf(entity) match {
      case Some(parent) => isVisible(parent)
      case _ => false
    }

  }

  def makeVisible(entity: Entity) = {

    entity match {
      case entity: Project =>
        setViewRoot(entity)
      case _ =>
        controller.model.parentOf(entity) match {
          case Some(parent) => setViewRoot(parent)
          case _ => setViewRoot(entity)
        }
    }

  }

  def selectedViews(): Seq[AbstractView with Selectable] = {
    getAllViews[Selectable].filter(_.isSelected())
  }

  def hoveredViews(): Seq[AbstractView with Selectable] = {
    getAllViews[Selectable].filter(_.isHovered())
  }

  def getCachedViews(): HashMap[EntityIdBase, AbstractView] = {
    val out = new HashMap[EntityIdBase, AbstractView]
    for (v <- views) {
      out.put(v._1.getId(), v._2)
    }
    out
  }

  def injectView(e: Entity, v: AbstractView) {
    views.put(e, v)
    v.register()
  }

  def setSelectedAsRootView() {
    if (controller.checkHasExactlySelected(1)) {
      val e = controller.selectedEntities()(0)

      if (e eq root) {
        resetView()
      } else {

        e match {
          case e: Project =>
            setViewRoot(e)
          case e: Module =>
            setViewRoot(e)
          case _ =>
            popupPreconditionFailed("Can only zoom in on Projects or Modules")
        }
      }

    }
  }

  def goBack() {
    movingThroughHistory.active {
      viewHistory.goBack(root.getId()) foreach {
        setViewRoot
      }
    }
  }

  def goForward() {
    movingThroughHistory.active {
      viewHistory goForward (root.getId()) foreach setViewRoot
    }
  }

  def goUp() {
    controller.model.parentOf(root) match {
      case Some(parent) => setViewRoot(parent)
      case _ =>
    }
  }

  def resetView() {
    setViewRoot(controller.model.project)
  }

  def setScaleFactor(factor: Double) {

    _scaleFactor = factor

    if (!controller.isBulkOperationActive) {

      controller.model.foreach(_ match {
        case ChildParent(child: PlacedEntity, _) => getView(child).updateBounds()
        case _ =>
      })

      topView.validate()
      topView.repaint()
    }

    controller.triggerObservers(_.onScaleFactorChanged())

  }

  def offsetScaleFactorIndex(deltaIndex: Int) {
    val newIndex = ZoomLevels.findClosestIndex(scaleFactor) + deltaIndex
    if (0 <= newIndex && newIndex < ZoomLevels.levels.size) {
      val newScale = ZoomLevels.levels(newIndex)
      setScaleFactor(newScale)
    }
  }

  def resetScaleFactor() {
    setScaleFactor(1.0)
  }

  def isInsideDashBoard(p: UiPos): Boolean = {
    UiPos.getScreenBoundsOfComp(topContainer).contains(p.onScreen)
  }

  def getViewHierarchy(entity: Entity): Seq[AbstractView] = {
    def gvh(view: AbstractView): List[AbstractView] = {
      parentOf(view) match {
        case Some(parentView) => view :: gvh(parentView)
        case _ => List(view)
      }
    }
    gvh(getView(entity)).reverse
  }

  def find(entity: Entity) {

    if (isRoot(entity)) {
      popupPreconditionFailed("You tried to find the view root")
      return
    }

    if (!isVisible(entity))
      makeVisible(entity)

    controller.select(entity, true, false)

    val hierarchy = getViewHierarchy(entity)
    val tgt = getView(entity)

    for (
      _view <- hierarchy.tail.reverse;
      parentView <- parentOf(_view)
    ) {

      import se.culvertsoft.mgen.visualdesigner.util.AwtMath._

      val pad = 30

      // Get child into visible area by minimal effort
      val scrollpane = parentView.asInstanceOf[ScrollableView].scrollpane
      val hbar = scrollpane.getHorizontalScrollBar
      val vbar = scrollpane.getVerticalScrollBar
      val tgtPosOnCurPane = UiPos.getCompBoundsFromCompBounds(tgt.bounds(), tgt.parentComponent(), scrollpane)

      val left = tgtPosOnCurPane.x - pad
      val right = tgtPosOnCurPane.x + tgtPosOnCurPane.width + pad
      val top = tgtPosOnCurPane.y - pad
      val bottom = tgtPosOnCurPane.y + tgtPosOnCurPane.height + pad

      val hCorrection =
        if (right > scrollpane.getWidth) {
          right - scrollpane.getWidth
        } else if (left < 0) {
          left
        } else {
          0
        }
      hbar.setValue(hbar.getValue + hCorrection)

      val vCorrection =
        if (top < 0) {
          top
        } else if (bottom > scrollpane.getHeight) {
          bottom - scrollpane.getHeight
        } else {
          0
        }
      vbar.setValue(vbar.getValue + vCorrection)

    }

    // TODO: Animate
    // Just spawn a new thread an perform the above ops over 1 sec, 
    // maybe with a logarithmic motion

  }

  override def onNewModel() {
    viewHistory.clear(controller.model.project.getId())
  }

  def findSelected() {
    if (controller.checkHasExactlySelected(1)) {
      find(controller.selectedEntities()(0))
    }
  }

  def findByName() {
    import se.culvertsoft.mgen.visualdesigner.util.AwtMath._
    val mousePos =
      controller.mouseInputMgr.mousePos.onScreen - searchDialog.getSize / 2

    searchDialog.setLocation(mousePos)

    if (!searchDialog.isVisible()) {
      searchDialog.setVisible(true)
    } else {
      searchDialog.toFront()
      searchDialog.repaint()
    }

    searchDialog.forwardFocus()

  }

  def configureViewOnLoadedNewFile() {
    def setToLowestChildWithNonMultipleChildren(child: Entity) {
      val children = child.firstLevelChildren
      if (children.size == 1 && children.head.isInstanceOf[Module]) {
        setToLowestChildWithNonMultipleChildren(children.head)
      } else {
        setViewRoot(child)
      }
    }
    setToLowestChildWithNonMultipleChildren(controller.model.project)
    setScaleFactor(0.5)
  }

}

