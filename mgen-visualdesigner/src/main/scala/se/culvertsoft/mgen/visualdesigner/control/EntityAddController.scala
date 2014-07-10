package se.culvertsoft.mgen.visualdesigner.control

import java.awt.Point
import java.awt.Rectangle
import javax.swing.JOptionPane
import se.culvertsoft.mgen.visualdesigner.EntityFactory
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.CustomTypeField
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.Module
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.util.Asof.RichFilterable
import se.culvertsoft.mgen.visualdesigner.util.LayOutEntities
import se.culvertsoft.mgen.visualdesigner.view.ModuleView
import se.culvertsoft.mgen.visualdesigner.model.FilePath
import se.culvertsoft.mgen.compiler.defaultparser.FileUtils
import se.culvertsoft.mgen.visualdesigner.model.EnumType
import se.culvertsoft.mgen.visualdesigner.model.EnumEntry

class EntityAddController(controller: Controller) extends SubController(controller) {

  def add(child: Entity, parent: Entity) {

    if (parent != null && !child.hasParent()) {
      if (!parent.containsAtFirstLevel(child))
        parent.add(child)
      child.setParent(parent.getId())
    }

    controller.triggerObservers(_.onEntityAdded(child, parent))

  }

  def add(child: Entity) {
    add(child, controller.model.project)
  }

  def transfer(child: Entity, newParent: Entity, oldParent: Entity) {

    oldParent.remove(child)
    newParent.add(child)
    child.setParent(newParent.getId())

    controller.triggerObservers(_.onEntityTransferred(child, newParent, oldParent))
  }

  def getNewModuleSaveDir(): FilePath = {
    val writtenDir = FileUtils.directoryOf(controller.model.project.getFilePath().getWritten())
    val absoluteDir = FileUtils.directoryOf(controller.model.project.getFilePath().getAbsolute())
    new FilePath(writtenDir, absoluteDir)
  }

  def addModule() {

    val module = EntityFactory.mkModule("NewModule", getNewModuleSaveDir())
      .setSettings(new java.util.HashMap)
      .setSubmodules(new java.util.ArrayList)
      .setTypes(new java.util.ArrayList)

    val (parent, position) = findPositionForNewModule(module.getPlacement().getWidth(), module.getPlacement().getHeight())

    module
      .getPlacement()
      .setX(position.x)
      .setY(position.y)

    parent.add(module)
    module.setParent(parent.getId())

    add(module, parent)

    controller.select(parent, true)

  }

  def addEnum(): Option[EnumType] = {

    if (!controller.checkHasExactlySelected(1))
      return None

    if (!controller.checkAllSelectedAreOfType[Module]("Classes can only be added to Modules. Select a module before trying to add a class."))
      return None

    val e = EntityFactory.mkEnum("NewEnum")

    val (parent, position) = findPositionForNewModule(
      e.getPlacement().getWidth(),
      e.getPlacement().getHeight())

    parent match {
      case parent: Module =>
        e
          .getPlacement()
          .setX(position.x)
          .setY(position.y)
        return Some(addEnum(e, parent))
    }

    return None

  }

  def addEnum(e: EnumType, parent: Module): EnumType = {

    parent.add(e)
    e.setParent(parent.getId())

    add(e, parent)

    controller.select(parent, true)

    e
  }

  def addType(): Option[CustomType] = {

    if (!controller.checkHasExactlySelected(1))
      return None

    if (!controller.checkAllSelectedAreOfType[Module]("Classes can only be added to Modules. Select a module before trying to add a class."))
      return None

    val cls = EntityFactory.mkClass("NewType")

    val (parent, position) = findPositionForNewModule(
      cls.getPlacement().getWidth(),
      cls.getPlacement().getHeight())

    parent match {
      case parent: Module =>
        cls
          .getPlacement()
          .setX(position.x)
          .setY(position.y)
        return Some(addType(cls, parent))
    }

    return None

  }

  def addType(cls: CustomType, parent: Module): CustomType = {

    parent.add(cls)
    cls.setParent(parent.getId())

    add(cls, parent)

    controller.select(parent, true)

    cls
  }

  def addField(t: CustomType, name: String) {

    val f = EntityFactory.mkField(name)

    f.setParent(t.getId())
    t.add(f)

    add(f, t)

    controller.select(t, true)

  }

  def addEntry(t: EnumType, name: String) {

    val f = EntityFactory.mkEnumEntry(name, null)

    f.setParent(t.getId())
    t.add(f)

    add(f, t)

    controller.select(t, true)

  }

  def addFieldOrEntry() {
    if (controller.checkHasExactlySelected(1)) {
      val e = controller.selectedEntities()(0)
      e match {
        case t: CustomTypeField =>
          controller.model().parentOf(t) match {
            case Some(t: CustomType) => addField(t, "newField")
            case _ =>
          }
        case t: CustomType => addField(t, "newField")
        case t: EnumEntry =>
          controller.model().parentOf(t) match {
            case Some(t: EnumType) => addEntry(t, "newEnumEntry")
            case _ =>
          }
        case t: EnumType => addEntry(t, "newEnumEntry")
        case _ => controller.viewMgr.popupPreconditionFailed("You need to select a class or enum to add a field/entry.")
      }
    }
  }

  private def boundsOf(e: Entity): Rectangle = {
    e match {
      case e: PlacedEntity => new Rectangle(
        e.getPlacement().getX(),
        e.getPlacement().getY(),
        e.getPlacement().getWidth(),
        e.getPlacement().getHeight())
      case _ =>
        new Rectangle(0, 0, 0, 0)
    }
  }

  def isSpaceAvailable(parent: Entity, _space: Rectangle, pad: Int = 20): Boolean = {
    val space = new Rectangle(_space.x - pad, _space.y - pad, _space.width + 2 * pad, _space.height + 2 * pad)
    parent.firstLevelChildren().forall(!boundsOf(_).intersects(space))
  }

  def findFirstFreePosition(parent: Entity, w: Int, h: Int): Point = {

    var x = LayOutEntities.DEFAULT_WALL_OFFSET_X
    var y = LayOutEntities.DEFAULT_WALL_OFFSET_Y

    val placements = parent.firstLevelChildren.filterOfType[PlacedEntity]().map(_.getPlacement())
    while (placements.exists(p => p.getX == x)) {
      x += LayOutEntities.DEFAULT_CLASS_WIDTH_PADDED
    }

    return new Point(x, y)

    val firstLvlChildren = parent.firstLevelChildren()

    if (firstLvlChildren.isEmpty)
      return new Point(15, 40)

    val xMax = math.max(5, firstLvlChildren.map(p => boundsOf(p).x /*+ p.width()*/ ).max)
    val yMax = math.max(15, firstLvlChildren.map(p => boundsOf(p).y /* + p.height()*/ ).max)

    val whRatio = 2

    // First try a random position spanned by the box above
    // Try 25 times
    for (extension <- 0 until 100) {
      for (iy <- 0 until math.max(1, yMax / w)) {
        for (ix <- 0 until math.max(1, xMax / w)) {

          var xr = 0.0
          var yr = 0.0

          if (math.random > 0.5) {
            xr = 1.0
          } else {
            yr = 1.0
          }

          val x = 5 + ix * w + extension * whRatio * 100 + math.random * w * whRatio * xr
          val y = 5 + iy * h + extension * 100 + math.random * h * yr
          val rect = new Rectangle(x.toInt, y.toInt, w, h)
          if (isSpaceAvailable(parent, rect, 20)) {
            return rect.getLocation()
          }
        }
      }
    }

    // Failed to find free space
    new Point(15, 40)

  }

  def findPositionForNewModule(w: Int, h: Int): (Entity, Point) = {

    val parent =
      controller.viewMgr.getAllViews[ModuleView]().filter(_.isSelected()).headOption match {
        case Some(parentModule) => parentModule.entity
        case None => controller.viewMgr.root()
      }

    (parent, findFirstFreePosition(parent, w, h))
  }

  def getSelectedEntitiesForDelete(): Seq[Entity] = {
    val selected = selectedEntities()
    val children = selected.flatMap(_.allChildren())
    (selected ++ children).distinct.filterNot(_ eq controller.model.project)
  }

  def deleteSelection(confirm: Boolean = true) {

    val entities = getSelectedEntitiesForDelete()

    controller.bulkOperation {

      def confirmDialog(): Boolean = {

        val answer = controller.viewMgr.showConfirmDialog(
          "Do you wish to delete selected entities including children, and orphan any excluded subtypes?",
          "Confirm deletion",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE)

        answer match {
          case JOptionPane.YES_OPTION => true
          case _ => false
        }

      }

      if (!confirm || confirmDialog()) {
        entities.reverse.foreach(delete(_))
      }

    }

    controller.triggerObservers(_.onModelModified())

  }

  def delete(entity: Entity) {

    val parent = controller.model.parentOf(entity)

    controller.bulkOperation {

      controller.model.detachParentOf(entity)

      entity match {
        case entity: CustomType =>
          controller.model.detachSuperTypeOf(entity)
          controller.model.detachSubTypesOf(entity)
        case _ =>
      }

      controller.model.removeFromCache(entity)

    }

    controller.triggerObservers(_.onEntityDeleted(entity, parent))

  }

}