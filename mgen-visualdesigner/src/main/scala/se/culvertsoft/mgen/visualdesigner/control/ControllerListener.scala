package se.culvertsoft.mgen.visualdesigner.control

import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity

trait ControllerListener {

  /**
   * ****************************
   *
   *
   * 		MODEL CHANGES
   *
   * ***************************
   */

  def onEntityAdded(child: Entity, parent: Entity) {}

  def onEntityTransferred(child: Entity, newParent: Entity, oldParent: Entity) {}

  def onEntityDeleted(child: Entity, parent: Option[Entity]) {}

  def onEntityMoved(entity: PlacedEntity, parent: Entity) {}

  def onEntityResized(entity: PlacedEntity, parent: Entity) {}

  def onEntityModified(child: Entity, validate: Boolean = false, parent: Option[Entity] = None) {}

  def onChildrenReordered(parent: Entity) {}

  def onModelCleared() {}

  def onModelModified(isPreview: Boolean) {}

  def onNewModel() {}

  final def onModelModified() { onModelModified(false) }

  /**
   * ****************************
   *
   *
   * 		VIEW CHANGES
   *
   * ***************************
   */

  def onScaleFactorChanged() {}
  
  def onIconOverrideChanged() {}

  def onViewRootChanged() {}

  def onFocusGained(entity: Entity) {}

  def onFocusLost(entity: Entity) {}

  def onSelectionChanged(selected: Seq[Entity], focused: Option[Entity]) {}

}
