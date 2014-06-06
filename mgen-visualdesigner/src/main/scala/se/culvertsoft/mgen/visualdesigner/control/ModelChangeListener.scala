package se.culvertsoft.mgen.visualdesigner.control

import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity

trait ModelChangeListener extends ControllerListener {

  override def onModelModified(isPreview: Boolean)

  override def onEntityAdded(child: Entity, parent: Entity) {
    onModelModified(false)
  }

  override def onEntityTransferred(child: Entity, newParent: Entity, oldParent: Entity) {
    onModelModified(false)
  }

  override def onEntityDeleted(child: Entity, parent: Option[Entity]) {
    onModelModified(false)
  }

  override def onEntityMoved(entity: PlacedEntity, parent: Entity) {
    onModelModified(false)
  }

  override def onEntityResized(entity: PlacedEntity, parent: Entity) {
    onModelModified(false)
  }

  override def onEntityModified(child: Entity, validate: Boolean = false, parent: Option[Entity] = None) {
    onModelModified(false)
  }

  override def onChildrenReordered(parent: Entity) {
    onModelModified(false)
  }

  override def onModelCleared() {
    onModelModified(false)
  }

  override def onNewModel() {
    onModelModified(false)
  }

}
