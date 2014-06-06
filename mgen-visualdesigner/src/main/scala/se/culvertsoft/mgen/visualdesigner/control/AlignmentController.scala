package se.culvertsoft.mgen.visualdesigner.control

import se.culvertsoft.mgen.visualdesigner.model.Entity
import se.culvertsoft.mgen.visualdesigner.model.ModelOps.toRichCustomType
import se.culvertsoft.mgen.visualdesigner.model.PlacedEntity
import java.awt.Point
import java.awt.Rectangle

class AlignmentController(controller: Controller) extends SubController(controller) {
   import se.culvertsoft.mgen.visualdesigner.model.RichPlacedEntity._

   private case class EBounds(
      val xMin: Int,
      val xCenter: Int,
      val xMax: Int,
      val yMin: Int,
      val yCenter: Int,
      val yMax: Int)

   private def align(
      fx: (PlacedEntity, EBounds) => Int,
      fy: (PlacedEntity, EBounds) => Int) {

      if (controller.checkAtLeastSelected(2) &&
         controller.checkSelectionHaveSameParent() &&
         controller.checkAllSelectedAreOfType[PlacedEntity]()) {

         val entities = controller.selectedPlacedEntities
         val parentEntity = controller.model.parentOf(entities(0)).get
         val views = controller.viewMgr.selectedViews
         val parentView = views(0).getParent.get

         val xMax = entities.map(_.right()).max
         val xMin = entities.map(_.x()).min
         val xCenter = (xMax + xMin) / 2
         val yMax = entities.map(_.bottom()).max
         val yMin = entities.map(_.y()).min
         val yCenter = (yMax + yMin) / 2

         val eb = EBounds(xMin, xCenter, xMax, yMin, yCenter, yMax)

         controller.bulkOperation {
            for (entity <- entities) {
               val x = fx(entity, eb)
               val y = fy(entity, eb)
               controller.boundsMgr.moveTo(entity, parentEntity, x, y, false)
            }
         }

         controller.triggerObservers(_.onModelModified())

      }
   }

   def alignSelectionXLeft() {
      align(
         (e, eb) => eb.xMin,
         (e, eb) => e.y)
   }

   def alignSelectionXCenter() {
      align(
         (e, eb) => eb.xCenter - e.width / 2,
         (e, eb) => e.y)
   }

   def alignSelectionXRight() {
      align(
         (e, eb) => eb.xMax - e.width,
         (e, eb) => e.y)
   }

   def alignSelectionYTop() {
      align(
         (e, eb) => e.x,
         (e, eb) => eb.yMin)
   }

   def alignSelectionYCenter() {
      align(
         (e, eb) => e.x,
         (e, eb) => eb.yCenter - e.height / 2)

   }

   def alignSelectionYBottom() {
      align(
         (e, eb) => e.x,
         (e, eb) => eb.yMax - e.height)
   }

}