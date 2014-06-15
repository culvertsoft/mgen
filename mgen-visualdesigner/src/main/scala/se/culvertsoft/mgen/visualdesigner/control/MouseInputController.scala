package se.culvertsoft.mgen.visualdesigner.control

import java.awt.Cursor
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent

import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.model.RichPlacedEntity.RichPlacedEnt
import se.culvertsoft.mgen.visualdesigner.util.Asof.RichFilterable
import se.culvertsoft.mgen.visualdesigner.util.AwtMath.RichDimension
import se.culvertsoft.mgen.visualdesigner.util.AwtMath.RichPoint
import se.culvertsoft.mgen.visualdesigner.util.LeftMouseButton
import se.culvertsoft.mgen.visualdesigner.util.MiddleMouseButton
import se.culvertsoft.mgen.visualdesigner.util.RichMouseEvent.RichMouseEventOps
import se.culvertsoft.mgen.visualdesigner.view.AbstractView
import se.culvertsoft.mgen.visualdesigner.view.ClassView
import se.culvertsoft.mgen.visualdesigner.view.Resizeable.ResizePoint
import se.culvertsoft.mgen.visualdesigner.view.ScrollableView
import se.culvertsoft.mgen.visualdesigner.view.Selectable

class MouseInputController(controller: Controller) extends SubController(controller) {

  private var _lastMousePressPos = new UiPos()
  private var _mousePos = new UiPos()
  private var _lastMousePos = new UiPos()
  private var _mouseDragAction: MouseDragAction = NoMouseDragAction
  private var _cursor: Cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
  private var _pendingNewCursor: Cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)

  def mousePos() = _mousePos
  def lastMousePressPos() = _lastMousePressPos
  def mouseDragAction() = _mouseDragAction
  def isMouseDragActionActive() = mouseDragAction() != NoMouseDragAction
  def setCursor(id: Int) { setCursor(Cursor.getPredefinedCursor(id)) }

  def filterGlobalEvent(mouseEvent: MouseEvent): Boolean = {
    val p = new UiPos(mouseEvent)
    if (p != mousePos()) {
      _lastMousePos = _mousePos
      _mousePos = p
    }

    if (mouseEvent.isBackBtn) {
      if (mouseEvent.isPress)
        controller.viewMgr.goBack()
      return false
    } else if (mouseEvent.isForwardBtn) {
      if (mouseEvent.isPress)
        controller.viewMgr.goForward()
      return false
    } else if (mouseEvent.isMiddleBtn) {
      if (mouseEvent.isPress) {
        startMousePanAction(mouseEvent)
      } else if (mouseEvent.isDrag) {
        handleMouseDragAction(mouseEvent)
      } else if (mouseEvent.isRelease) {
        finishMouseAction()
      }
      return false
    }

    mouseEvent.getID() match {
      case MouseEvent.MOUSE_PRESSED =>
        _lastMousePressPos = new UiPos(mouseEvent)
      case MouseEvent.MOUSE_RELEASED =>
        finishMouseAction()
      case MouseEvent.MOUSE_DRAGGED =>
        handleMouseDragAction(mouseEvent)
      case _ =>
        mouseEvent match {
          case mouseEvent: MouseWheelEvent =>
            if (mouseEvent.isControlDown()) {
              return scaleByMouseWheel(mouseEvent)
            }
          case _ =>
        }
    }
    true
  }

  def setMouseDragAction(action: MouseDragAction) {
    _mouseDragAction = action
    if (action != NoMouseDragAction)
      _pendingNewCursor = _cursor
  }

  def startMouseMoveAction(entity: PlacedEntity) {

    if (!isMouseDragActionActive() &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val p = lastMousePressPos()
      val entityPos = controller.viewMgr.getUiPosOf(entity)
      val selectedEntities = controller.selectedPlacedEntities
      val selectedViews = selectedEntities map controller.viewMgr.view
      val entitiesToMove = selectedViews.map(x =>
        new MouseMoveEntity(
          p,
          controller.viewMgr.getUiPosOf(x.entity),
          x.entity.asInstanceOf[PlacedEntity]))
      val ents =
        setMouseDragAction(new MouseMoveAction(entitiesToMove))
    }
  }

  def startMouseResizeAction(e: MouseEvent, rp: ResizePoint, entity: PlacedEntity) {

    if (!isMouseDragActionActive() &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val p = lastMousePressPos()
      val entityPos = controller.viewMgr.getUiPosOf(entity)

      setMouseDragAction(new MouseResizeAction(p, entity.bounds(), entity, rp))
    }
  }

  def startMouseSelectionBoxAction(e: MouseEvent, parent: Entity) {
    if (!isMouseDragActionActive()) {
      val p = new UiPos(e)
      setMouseDragAction(new MouseSelectionBoxAction(p, parent))
    }
  }

  def handleMouseDragAction(mouseEvent: MouseEvent) {
    mouseEvent.button match {
      case LeftMouseButton => handleMouseLeftBtnDrag(mouseEvent)
      case MiddleMouseButton => handleMouseMiddleBtnDrag(mouseEvent)
      case _ =>
    }
  }

  def handleMouseLeftBtnDrag(mouseEvent: MouseEvent) {
    mouseDragAction() match {
      case moveAction: MouseMoveAction =>
        handleMouseDragMoveEntity(mouseEvent, moveAction)
      case resizeAction: MouseResizeAction =>
        handleMouseDragResizeEntity(mouseEvent, resizeAction)
      case selectionBoxAction: MouseSelectionBoxAction =>
        handleMouseSelectionBox(mouseEvent, selectionBoxAction)
      case createSubtypeAction: MouseDragCreateSubTypeAction =>
        handleCreateSubTypeAction(mouseEvent, createSubtypeAction)
      case attachSuperTypeAction: MouseDragAttachSuperTypeAction =>
        handleAttachSuperTypeAction(mouseEvent, attachSuperTypeAction)
      case _ =>
    }
  }
  def handleMouseMiddleBtnDrag(mouseEvent: MouseEvent) {
    mouseDragAction() match {
      case action: MousePanAction =>
        handleMousePanAction(mouseEvent, action)
      case _ =>
    }
  }

  def handleMouseDragMoveEntity(mouseEvent: MouseEvent, _action: MouseMoveAction) {

    controller.bulkOperation {

      _action.entities.foreach { action =>

        val view = controller.viewMgr.view(action.entity)

        val oldParent = controller.model.parentOf(action.entity).get
        val entityMouseOffset = action.entityInitPos.onScreen - action.initPos.onScreen
        val pEntityNewPtOnScreen = mousePos().onScreen + entityMouseOffset
        val pEntityNewMidPtOnScreen = pEntityNewPtOnScreen + view.halfSize
        val parent = controller.findNewParentFor(pEntityNewMidPtOnScreen, action.entity)

        parent.filterNot(_ eq oldParent) match {

          case Some(newParent) =>

            val newParentView = controller.viewMgr.view(newParent)
            val pNewNewComp = UiPos.getCompCoordFromScreen(pEntityNewPtOnScreen, newParentView.innerPanel)

            controller.entityAddMgr.transfer(action.entity, newParent, oldParent)
            controller.boundsMgr.moveTo(
              action.entity,
              newParent,
              (pNewNewComp.x / view.scaleFactor).toInt,
              (pNewNewComp.y / view.scaleFactor).toInt,
              false)

          case None =>
            val oldParentView = controller.viewMgr.view(oldParent)
            val pEntityNewPtOnComp = UiPos.getCompCoordFromScreen(pEntityNewPtOnScreen, oldParentView.innerPanel)
            controller.boundsMgr.moveTo(
              action.entity,
              oldParent,
              (pEntityNewPtOnComp.x / view.scaleFactor).toInt,
              (pEntityNewPtOnComp.y / view.scaleFactor).toInt,
              true)

        }

      }

    }

    controller.triggerObservers(_.onModelModified(true))

  }

  def handleMouseDragResizeEntity(mouseEvent: MouseEvent, action: MouseResizeAction) {

    val parent = controller.model.parentOf(action.entity).get
    val view = controller.viewMgr.view(action.entity)

    controller.bulkOperation {

      val delta_unsigned = mousePos().onScreen - action.initPos.onScreen
      val delta = new Dimension(
        (delta_unsigned.x * action.resizePoint.xSign.toInt / view.scaleFactor).toInt,
        (delta_unsigned.y * action.resizePoint.ySign.toInt / view.scaleFactor).toInt)
      val newSize = action.entityInitBounds.getSize() + delta

      controller.boundsMgr.resizeTo(
        action.entity,
        action.entityInitBounds,
        parent,
        newSize.width,
        newSize.height,
        action.resizePoint)

    }

    controller.triggerObservers(_.onModelModified(true))

  }

  def handleMouseSelectionBox(mouseEvent: MouseEvent, action: MouseSelectionBoxAction) {

    controller.bulkOperation {

      controller.viewMgr.getAllViews[Selectable]().filter(_.isHovered()).foreach(_.setHovered(false))

      val hoverableChildren = action
        .containerEntity
        .firstLevelChildren()
        .map(controller.viewMgr.view)
        .filterOfType[Selectable]()
        .asInstanceOf[Seq[AbstractView with Selectable]]

      hoverableChildren.foreach(_.setHovered(false))

      val pOld = action.initPos.onComponent
      val pNew = UiPos.getCompCoordFromScreen(mouseEvent.getLocationOnScreen(), action.initPos.component)

      val x0 = math.min(pOld.x, pNew.x)
      val y0 = math.min(pOld.y, pNew.y)

      val x1 = math.max(pOld.x, pNew.x)
      val y1 = math.max(pOld.y, pNew.y)

      val w = x1 - x0
      val h = y1 - y0

      val selectionBox = new Rectangle(x0, y0, w, h)

      hoverableChildren
        .filter(e => selectionBox.contains(e.midPt()))
        .foreach(_.setHovered(true))

    }

    controller.triggerObservers(_.onEntityModified(action.containerEntity))

  }

  def finishSelectionBoxAction(action: MouseSelectionBoxAction) {

    controller.bulkOperation {
      val selected = controller.viewMgr.getAllViews[Selectable]().filter(_.isSelected())
      val hovered = controller.viewMgr.getAllViews[Selectable]().filter(_.isHovered())
      if (hovered.nonEmpty) {
        if (!controller.keyboardInputMgr.isControlDown())
          selected.foreach(_.setSelected(false))
        hovered.foreach(_.setHovered(false))
        hovered.foreach(_.setSelected(true))
        controller.triggerObservers(_.onSelectionChanged(selectedEntities(), selectedEntities().headOption))
      }
    }

    controller.triggerObservers(_.onEntityModified(action.containerEntity))

  }

  def finishMouseAction() {
    if (isMouseDragActionActive()) {
      if (mousePos.onScreen != lastMousePressPos.onScreen) {
        mouseDragAction match {
          case action: MouseMoveAction => finishMouseMoveAction(action)
          case action: MouseResizeAction => finishMouseResizeAction(action)
          case action: MousePanAction => finishMousePanAction(action)
          case action: MouseSelectionBoxAction => finishSelectionBoxAction(action)
          case action: MouseDragAttachSuperTypeAction => finishAttachSuperTypeAction(action)
          case _ =>
        }
      }
      setMouseDragAction(NoMouseDragAction)
      setCursor(_pendingNewCursor)

    }
  }

  def finishMouseMoveAction(action: MouseMoveAction) {
    controller.triggerObservers(_.onModelModified())
  }

  def finishMouseResizeAction(action: MouseResizeAction) {
    controller.triggerObservers(_.onModelModified())
  }

  def finishMousePanAction(action: MousePanAction) {

  }

  def setCursor(c: Cursor) {
    _pendingNewCursor = c
    if (!isMouseDragActionActive && c != _cursor) {
      _cursor = c
      controller.viewMgr.setCursor(c)
    }
  }

  def isHoverInhibitActive(): Boolean = {
    mouseDragAction().isInstanceOf[MouseSelectionBoxAction]
  }

  def startMouseDragAttachSuperTypeAction(subType: CustomType, e: MouseEvent) {
    if (!isMouseDragActionActive()) {
      println("startMouseDragAttachSuperTypeAction")
      val uiPos = new UiPos(e)
      val action = new MouseDragAttachSuperTypeAction(uiPos, subType)
      setMouseDragAction(action)
    }
  }

  def findDeepestCurrentlyScrollableViewAt(uiPos: UiPos): ScrollableView = {
    var out = controller.viewMgr.rootView
    val screenPos = uiPos.onScreen
    controller.viewMgr.findDeepestEntityAt(
      screenPos,
      x => {
        controller.viewMgr.view(x) match {
          case x: ScrollableView =>
            if (x.scrollpane.getHorizontalScrollBar().isShowing() || x.scrollpane.getVerticalScrollBar().isShowing()) {
              out = x
              true
            } else
              false
          case _ =>
            false
        }
      },
      x => controller.viewMgr.isVisible(x))
    out
  }

  def startMousePanAction(e: MouseEvent) {
    if (!isMouseDragActionActive()) {
      val uiPos = new UiPos(e)
      val view = findDeepestCurrentlyScrollableViewAt(uiPos)
      val action = new MousePanAction(
        uiPos,
        view.scrollpane.getHorizontalScrollBar().getValue(),
        view.scrollpane.getVerticalScrollBar().getValue(),
        view)
      if (view.screenBounds.contains(uiPos.onScreen)) {
        setCursor(Cursor.MOVE_CURSOR)
        setMouseDragAction(action)
        setCursor(Cursor.DEFAULT_CURSOR)
      }
    }
  }

  def startMouseDragCreateSubTypeAction(superType: CustomType, e: MouseEvent) {
    if (!isMouseDragActionActive()) {
      println("startMouseDragCreateSubTypeAction")
      val uiPos = new UiPos(e)
      val action = new MouseDragCreateSubTypeAction(uiPos, superType)
      setMouseDragAction(action)
    }
  }

  def handleCreateSubTypeAction(mEvent: MouseEvent, selectionBoxAction: MouseDragCreateSubTypeAction) {

    val superType = selectionBoxAction.superType
    val parent = controller.model.parentOf(selectionBoxAction.superType).get.asInstanceOf[Module]

    val e = EntityFactory.mkClass(
      "NewType",
      superType.x,
      superType.y + superType.height,
      superType.width,
      superType.height)

    controller.model.attachSubType(e, superType)
    controller.entityAddMgr.addType(e, parent)

    val singleEntityMove =
      new MouseMoveEntity(
        lastMousePressPos(),
        controller.viewMgr.getUiPosOf(e),
        e)

    setMouseDragAction(NoMouseDragAction)
    setCursor(Cursor.MOVE_CURSOR)

    controller.select(e, true, false)

    setMouseDragAction(new MouseMoveAction(Seq(singleEntityMove)))
    setCursor(Cursor.DEFAULT_CURSOR)

  }

  def handleAttachSuperTypeAction(mEvent: MouseEvent, attachSuperTypeAction: MouseDragAttachSuperTypeAction) {

  }

  def finishPanAction(action: MousePanAction) {

  }

  def finishAttachSuperTypeAction(attachSuperTypeAction: MouseDragAttachSuperTypeAction) {

    val subType = attachSuperTypeAction.subType
    val hovered = controller.viewMgr.hoveredViews

    hovered.headOption match {
      case Some(superView: ClassView) => 
        controller.changeSuperType(subType, superView.entity)
      case _ =>
    }

  }

  def cancelMouseOperations() {
    if (isMouseDragActionActive()) {
      finishMouseAction()
      controller.undoMgr.undo()
    }
  }

  def scaleByMouseWheel(mouseEvent: MouseWheelEvent): Boolean = {
    if (controller.viewMgr.isInsideDashBoard(new UiPos(mouseEvent))) {

      // Check mouse pos before in view
      val mousePos0 = new UiPos(mouseEvent)
      val rootView = controller.viewMgr.rootView()
      val scrollpane = rootView.scrollpane
      val mousePosBefore = UiPos.getCompCoordFromScreen(mousePos0.onScreen, rootView.innerPanel) / controller.viewMgr.scaleFactor()

      // Perform the scaling
      controller.viewMgr.offsetScaleFactorIndex(-math.signum(mouseEvent.getWheelRotation()))

      // Check mouse pos after
      val mousePosAfter = UiPos.getCompCoordFromScreen(mousePos0.onScreen, rootView.innerPanel) / controller.viewMgr.scaleFactor()
      val entityError = mousePosBefore - mousePosAfter
      val scaledError = entityError * controller.viewMgr.scaleFactor()
      val scrollx = scrollpane.getHorizontalScrollBar().getValue()
      val scrolly = scrollpane.getVerticalScrollBar().getValue()

      // TODO: Check if we need to extend the view to support scrolling(!)
      // TODO: Or how do we handle this? Should we move the cursor instead?
      // wth ? ..... not sure how this should work!

      // Set the new scrollbar positions
      scrollpane.getHorizontalScrollBar().setValue(scrollx + scaledError.x)
      scrollpane.getVerticalScrollBar().setValue(scrolly + scaledError.y)

      false
    } else {
      true
    }
  }

  def handleMousePanAction(mouseEvent: MouseEvent, action: MousePanAction) {

    val delta = mouseEvent.getLocationOnScreen() - action.initPos.onScreen

    val newHbarPos = action.hBarInitValue - delta.x
    val newVbarPos = action.vBarInitValue - delta.y

    val scrollpane = action.view.scrollpane
    val hbar = scrollpane.getHorizontalScrollBar()
    val vbar = scrollpane.getVerticalScrollBar()

    if (hbar.isShowing())
      hbar.setValue(newHbarPos)

    if (vbar.isShowing())
      vbar.setValue(newVbarPos)

  }

}
