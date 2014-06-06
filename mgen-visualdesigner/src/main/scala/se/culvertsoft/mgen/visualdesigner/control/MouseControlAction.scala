package se.culvertsoft.mgen.visualdesigner.control

import java.awt.Rectangle
import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.view.Resizeable.ResizePoint
import se.culvertsoft.mgen.visualdesigner.model.CustomType
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import se.culvertsoft.mgen.visualdesigner.view.ScrollableView

abstract class MouseDragAction

case class MouseMoveEntity(
   val initPos: UiPos,
   val entityInitPos: UiPos,
   val entity: PlacedEntity)

case class MouseMoveAction(
   val entities: Seq[MouseMoveEntity]) extends MouseDragAction

case class MouseResizeAction(
   val initPos: UiPos,
   val entityInitBounds: Rectangle,
   val entity: PlacedEntity,
   val resizePoint: ResizePoint) extends MouseDragAction

case class MousePanAction(
   val initPos: UiPos,
   val hBarInitValue: Int,
   val vBarInitValue: Int,
   val view: ScrollableView) extends MouseDragAction

case class MouseSelectionBoxAction(
   val initPos: UiPos,
   val containerEntity: Entity) extends MouseDragAction

case class MouseDragCreateSubTypeAction(
   val initPos: UiPos,
   val superType: CustomType) extends MouseDragAction

case class MouseDragAttachSuperTypeAction(
   val initPos: UiPos,
   val subType: CustomType) extends MouseDragAction
   
case object NoMouseDragAction extends MouseDragAction
