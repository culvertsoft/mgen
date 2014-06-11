package se.culvertsoft.mgen.visualdesigner.control

import java.awt.AWTEvent
import java.awt.Point
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.reflect.ClassTag

import javax.swing.JFrame
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.EntityIdBase
import se.culvertsoft.mgen.visualdesigner.model.FieldType
import se.culvertsoft.mgen.visualdesigner.model.Model
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.util.Asof.RichFilterable
import se.culvertsoft.mgen.visualdesigner.util.Observable
import se.culvertsoft.mgen.visualdesigner.util.OperationStatus
import se.culvertsoft.mgen.visualdesigner.view.AbstractView
import se.culvertsoft.mgen.visualdesigner.view.ContentPane
import se.culvertsoft.mgen.visualdesigner.view.Selectable
import se.culvertsoft.mgen.visualdesigner.view.ViewManager

class Controller(

  /**
   * *****************************************************
   *
   *
   * 							STATE
   *
   * *****************************************************
   */

  private var _model: Model,
  private val topContainer: ContentPane,
  private val window: JFrame) extends Observable[ControllerListener] {
  private val bulkOperationStatus = new OperationStatus

  val boundsMgr = new BoundsController(this)
  val alignmentMgr = new AlignmentController(this)
  val entityAddMgr = new EntityAddController(this)
  val keyboardInputMgr = new KeyboardInputController(this)
  val mouseInputMgr = new MouseInputController(this)
  val copyPasteMgr = new CopyPasteController(this)
  val saveMgr = new SaveController(this, window)
  val undoMgr = new UndoController(this)
  val generateMgr = new GenerateController(this)
  val viewMgr = new ViewManager(this, topContainer)
  val settingsMgr = new SettingsController(this)

  setModel(_model, true)

  /**
   * *****************************************************
   *
   *
   * 							METHODS
   *
   * *****************************************************
   */

  def model() = {
    _model
  }

  def isBulkOperationActive(): Boolean = {
    bulkOperationStatus.isActive()
  }

  def bulkOperation(f: => Unit) {
    bulkOperationStatus active f
  }

  def selectedEntities(): Seq[Entity] = {
    viewMgr.selectedViews().map(_.entity)
  }

  def selectedPlacedEntities(): Seq[PlacedEntity] = {
    selectedEntities().filterOfType[PlacedEntity]
  }

  def deselectAll(exception: Entity) {

    bulkOperation {
      viewMgr
        .selectedViews()
        .filterNot(_ eq viewMgr.getView(exception))
        .foreach(_.setSelected(false))
    }

    triggerObservers(_.onSelectionChanged(selectedEntities(), None))

  }

  def select(entity: Entity, deselectOthers: Boolean = true, editFocus: Boolean = true) {

    bulkOperation {

      if (deselectOthers)
        deselectAll(entity)

      viewMgr.getView(entity) match {
        case s: Selectable if (!s.isSelected()) => s.setSelected(true)
        case _ =>
      }

    }

    triggerObservers(_.onSelectionChanged(selectedEntities(), if (editFocus) Some(entity) else None))

  }

  def hover(state: Boolean, s: Selectable, e: Entity) {

    if (mouseInputMgr.isHoverInhibitActive)
      return

    mouseInputMgr.mouseDragAction() match {
      case action: MouseDragAttachSuperTypeAction if state =>
        e match {
          case e: CustomType if !(e eq action.subType) => s.setHovered(true)
          case _ => s.setHovered(false)
        }
      case _ => s.setHovered(state)
    }

  }

  def deSelect(entity: Entity, deselectOthers: Boolean = true) {

    bulkOperation {

      if (deselectOthers)
        deselectAll(entity)

      viewMgr.getView(entity) match {
        case selectable: Selectable => selectable.setSelected(false)
        case _ =>
      }

    }

    triggerObservers(_.onSelectionChanged(selectedEntities(), None))

  }

  def filterGlobalEvent(e: AWTEvent): Boolean = {
    e match {
      case mouseEvent: MouseEvent => mouseInputMgr.filterGlobalEvent(mouseEvent)
      case keyEvent: KeyEvent => keyboardInputMgr.filterGlobalEvent(keyEvent)
      case _ => true
    }
  }

  def findNewParentFor(screenPos: Point, entity: Entity): Option[Entity] = {
    viewMgr.findDeepestEntityAt(
      screenPos,
      x => !(x eq entity) && viewMgr.isVisible(x) && x.canBeParentOf(entity),
      x => !(x eq entity) && viewMgr.isVisible(x))
  }

  def focusGained(entity: Entity) {
    triggerObservers(_.onFocusGained(entity))
  }

  def focusLost(entity: Entity) {
    triggerObservers(_.onFocusLost(entity))
  }

  def getKeepableClassViews(oldModel: Model, newModel: Model): Seq[(Entity, AbstractView)] = {

    // The whole purpose of this algorithm is to prevent
    // The creation of new class objects and especially
    // new java Swing objects when the old entities
    // can be reused.

    oldModel.updateCache()
    newModel.updateCache()

    val oldClasses = oldModel.findEach[CustomType]()
    val newClasses = newModel.findEach[CustomType]()

    val out = new ArrayBuffer[(Entity, AbstractView)]

    // Find classes that can be replaced with old ones
    for (newClass <- newClasses) {
      oldModel.getEntity(newClass.getId()) match {
        case Some(oldClass: CustomType) =>

          // Begin by setting all old placements to the new ones
          oldClass.setPlacement(newClass.getPlacement())

          // Now check if they are equal (scala: "==" is ok)
          if (oldClass == newClass) {

            // Replace newClass with oldClass
            val newParent = newModel.parentOf(newClass).get.asInstanceOf[Module]
            val idx = newParent.getTypes().indexOf(newClass)
            newParent.getTypes().set(idx, oldClass)

            val clsView = viewMgr.getView(oldClass)

            // Ensure the view is resized properly
            clsView.updateBounds()

            out += ((oldClass, clsView))

            for (oldField <- oldClass.getFields()) {
              val fldView = viewMgr.getView(oldField)
              out += ((oldField, fldView))
            }

          }

        case _ =>
      }
    }

    // So that the cache doesnt point to old types
    newModel.updateCache()

    out

  }

  def setModel(newModel: Model, clearUndoBuffer: Boolean, allowCaching: Boolean = true) {

    val oldModel = _model

    val keepableClassViews = if (allowCaching) getKeepableClassViews(oldModel, newModel) else Nil

    _model = newModel

    val prevViewRootId = viewMgr.root().getId()

    bulkOperation {

      viewMgr.setViewRootShallow(model.project)

      triggerObservers(_.onModelCleared())

      for (v <- keepableClassViews)
        viewMgr.injectView(v._1, v._2)

      viewMgr.root.foreach(cp => entityAddMgr.add(cp.child, cp.parent))

      if (clearUndoBuffer)
        undoMgr.resetUndoBuffer()

      if (keepableClassViews.nonEmpty) {
        val toMarkDirty = new HashMap[EntityIdBase, Entity]
        for ((e, _) <- keepableClassViews) {
          e match {
            case e: CustomType =>
              model.foreachReferencedClass(e) { x =>
                toMarkDirty.put(x.getId(), x)
              }
            case _ =>
          }
        }
        for ((_, e) <- toMarkDirty) {
          triggerObservers(_.onEntityModified(e, false, None))
        }
      }

    }

    model.getEntity(prevViewRootId) match {
      case Some(e) => viewMgr.setViewRoot(e)
      case None => viewMgr.setViewRoot(model.project)
    }

    triggerObservers(_.onModelModified())

  }

  def rebuildView(clearUndoBuffer: Boolean = false, allowCaching: Boolean = true) {
    setModel(model, clearUndoBuffer, allowCaching)
  }

  def selectAll() {

    bulkOperation {

      val parent =
        viewMgr.getAllViews[Selectable]()
          .filter(_.isSelected())
          .headOption
          .map(_.entity)
          .getOrElse(viewMgr.root())

      deselectAll(null)
      parent.foreachFirstLevelChild(cp => select(cp.child, false))

    }

    triggerObservers(_.onSelectionChanged(selectedEntities(), None))

  }

  def moveUp() {
    if (checkSelectionHaveSameParent()) {
      val selection = selectedEntities()
      val parent = model.parentOf(selection(0)).get
      bulkOperation { selection foreach parent.moveChildUp }
      triggerObservers(_.onChildrenReordered(parent))
    }
  }

  def moveDown() {
    if (checkSelectionHaveSameParent()) {
      val selection = selectedEntities()
      val parent = model.parentOf(selection(0)).get
      bulkOperation { selection.reverse foreach parent.moveChildDown }
      triggerObservers(_.onChildrenReordered(parent))
    }
  }

  def checkSelectedMeetCondition(errMsg: String)(condition: Entity => Boolean): Boolean = {
    if (selectedEntities.forall(condition)) {
      true
    } else {
      viewMgr.popupPreconditionFailed(errMsg)
      false
    }
  }

  def checkAllSelectedAreOfType[T: ClassTag](errMsg: String = "Not all selected entities matched the requirement"): Boolean = {
    checkSelectedMeetCondition(errMsg)(_ match {
      case t: T => true
      case _ => false
    })
  }

  def checkAtLeastSelected(n: Int): Boolean = {
    if (selectedEntities.size >= n) true
    else viewMgr.popupPreconditionFailed(s"You need to select at least $n item(s) to perform this operation.")
  }

  def checkAtMostSelected(n: Int): Boolean = {
    if (selectedEntities.size <= n) true
    else viewMgr.popupPreconditionFailed(s"You need to select at most $n item(s) to perform this operation.")
  }

  def checkHasExactlySelected(n: Int): Boolean = {
    checkAtLeastSelected(n) && checkAtMostSelected(n)
  }

  def checkSelectionHaveSameParent(): Boolean = {
    val parents = selectedEntities().map(_.getParent()).distinct
    if (parents.size == 1) {
      true
    } else if (parents.isEmpty) {
      viewMgr.popupPreconditionFailed("You need to select at least one item to perform this operation.")
    } else {
      viewMgr.popupPreconditionFailed("You may only select items with the same parent to perform this operation.")
    }
  }

  def renameSelection() {
    if (checkHasExactlySelected(1)) {
      val e = selectedEntities()(0)
      val c = viewMgr.getView(e).innerPanel
      viewMgr.popupGetString("Enter new name", e.getName(), c) match {
        case Some(name) => rename(e, name)
        case _ =>
      }
    }
  }

  def rename(e: Entity, newName: String) {
    if (newName != e.getName() && newName.trim.nonEmpty) {
      e.setName(newName)
      triggerObservers(_.onEntityModified(e, false, model.parentOf(e)))
    }
  }

  def changeType(parent: CustomTypeField, newType: FieldType) {
    if (newType != parent.getType()) {
      parent.setType(newType)
      triggerObservers(_.onEntityModified(parent, false, model.parentOf(parent)))
    }
  }

  def removeSuperTypeOf(subType: CustomType) {
    if (subType.hasSuperType()) {
      model.detachSuperTypeOf(subType)

      if (!isBulkOperationActive) {
        model.superTypeOf(subType) foreach { superType => triggerObservers(_.onEntityModified(superType, false, None)) }
        triggerObservers(_.onEntityModified(subType, false, None))
        triggerObservers(_.onModelModified())
      }
    }
  }

  def changeSuperType(subType: CustomType, superType: CustomType) {
    if (!subType.hasSuperType() || subType.getId() != superType.getSuperType()) {

      bulkOperation {
        removeSuperTypeOf(subType)
        model.attachSubType(subType, superType)
      }

      triggerObservers(_.onEntityModified(subType, false, None))
      triggerObservers(_.onModelModified())

    }
  }

  def changeFlags(parent: CustomTypeField, newFlags: java.util.ArrayList[String]) {
    if (newFlags != parent.getFlags()) {
      parent.setFlags(newFlags)
      triggerObservers(_.onEntityModified(parent, false, model.parentOf(parent)))
    }
  }

}
