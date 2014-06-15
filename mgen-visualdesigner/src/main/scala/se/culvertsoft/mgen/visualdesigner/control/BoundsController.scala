package se.culvertsoft.mgen.visualdesigner.control

import java.awt.Rectangle

import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.model.RichPlacedEntity.RichPlacedEnt
import se.culvertsoft.mgen.visualdesigner.util.LayOutEntities
import se.culvertsoft.mgen.visualdesigner.view.Resizeable.ResizePoint
import se.culvertsoft.mgen.visualdesigner.view.Resizeable.ResizePointSE

class BoundsController(controller: Controller) extends SubController(controller) {

  import se.culvertsoft.mgen.visualdesigner.model.RichPlacedEntity._

  def moveTo(entity: PlacedEntity, parent: Entity, _x: Int, _y: Int, clamped: Boolean) {

    val x = if (clamped) math.max(_x, 5) else _x
    val y = if (clamped) math.max(_y, 5) else _y

    entity.getPlacementMutable()
      .setX(x)
      .setY(y)

    controller.triggerObservers(_.onEntityMoved(entity, parent))
  }

  def resizeTo(
    entity: PlacedEntity,
    entityInitBounds: Rectangle,
    parent: Entity,
    _w: Int,
    _h: Int,
    rp: ResizePoint = ResizePointSE) {

    val view = controller.viewMgr.view(entity)

    val _dx = (entityInitBounds.width - _w) * rp.xMove
    val _dy = (entityInitBounds.height - _h) * rp.yMove

    val _x = entityInitBounds.x + _dx
    val _y = entityInitBounds.y + _dy

    val x2 = _x + _w
    val y2 = _y + _h

    var x = math.max(_x, 5)
    var y = math.max(_y, 5)
    val w = math.max(x2 - x, view.minWidth())
    val h = math.max(y2 - y, view.minHeight())

    if (w > _w) {
      x += (_w - w) * rp.xMove
    }

    if (h > _h) {
      y += (_h - h) * rp.yMove
    }

    entity.setPos(x, y)
    entity.setSize(w, h)

    controller.triggerObservers(_.onEntityResized(entity, parent))

  }

  private def spread(
    entities: Seq[PlacedEntity],
    start: Int,
    end: Int,
    isX: Boolean) {

    val step = (end - start).toDouble / (entities.size - 1).toDouble + 1e-6

    controller.bulkOperation {
      for ((e, i) <- entities.zipWithIndex) {
        val midPt = (start + step * i).toInt
        val parent = controller.model.parentOf(e).get
        if (isX) {
          moveTo(e, parent, midPt - e.width / 2, e.y, true)
        } else {
          moveTo(e, parent, e.x, midPt - e.height / 2, true)
        }
      }
    }

    controller.triggerObservers(_.onModelModified())
  }

  def spreadEqualY() {
    if (controller.checkSelectionHaveSameParent() &&
      controller.checkAtLeastSelected(3) &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {
      val selection = controller.selectedPlacedEntities
      val yMidStart = selection.map(_.midPt.y).min
      val yMidEnd = selection.map(_.midPt.y).max
      spread(selection, yMidStart, yMidEnd, false)
    }
  }

  def spreadEqualX() {

    if (controller.checkSelectionHaveSameParent() &&
      controller.checkAtLeastSelected(3) &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val selection = controller.selectedPlacedEntities
      val xMidStart = selection.map(_.midPt.x).min
      val xMidEnd = selection.map(_.midPt.x).max
      spread(selection, xMidStart, xMidEnd, true)
    }
  }

  def spaceOutY() {
    if (controller.checkSelectionHaveSameParent() &&
      controller.checkAtLeastSelected(2) &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val pad = 50
      val selection = controller.selectedPlacedEntities
      val yMidStart = selection.map(_.midPt.y).min
      val h = selection.map(_.height).max
      val yMidEnd = yMidStart + (selection.size - 1) * (h + pad)
      spread(selection, yMidStart, yMidEnd, false)
    }
  }

  def layOut(allowResize: Boolean) {
    if (controller.checkHasExactlySelected(1)) {
      val e = controller.selectedEntities()(0)
      LayOutEntities(e, controller.model, allowResize)

      controller.bulkOperation {
        e.foreach { cp =>
          cp.child match {
            case child: PlacedEntity =>
              controller.triggerObservers(_.onEntityMoved(child, cp.parent))
            case _ =>
          }
        }
      }

      controller.triggerObservers(_.onModelModified())

    }
  }

  def spaceOutX() {
    if (controller.checkSelectionHaveSameParent() &&
      controller.checkAtLeastSelected(2) &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val pad = 30
      val selection = controller.selectedPlacedEntities
      val xMidStart = selection.map(_.midPt.x).min
      val w = selection.map(_.width).max
      val xMidEnd = xMidStart + (selection.size - 1) * (w + pad)
      spread(selection, xMidStart, xMidEnd, true)
    }
  }

  def resizeSameWidth() {

    if (controller.checkAtLeastSelected(2) &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val selection = controller.selectedPlacedEntities
      val w = selection.map(_.width()).max

      controller.bulkOperation {
        for (e <- selection) {
          resizeTo(e, e.bounds(), controller.model.parentOf(e).get, w, e.height())
        }
      }

      controller.triggerObservers(_.onModelModified())

    }
  }

  def resizeSameHeight() {
    if (controller.checkAtLeastSelected(2) &&
      controller.checkAllSelectedAreOfType[PlacedEntity]()) {

      val selection = controller.selectedPlacedEntities
      val h = selection.map(_.height()).max
      controller.bulkOperation {
        for (e <- selection) {
          resizeTo(e, e.bounds(), controller.model.parentOf(e).get, e.width(), h)
        }
      }

      controller.triggerObservers(_.onModelModified())
    }
  }

}