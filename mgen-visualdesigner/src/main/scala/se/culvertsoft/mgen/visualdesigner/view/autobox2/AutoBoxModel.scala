package se.culvertsoft.mgen.visualdesigner.view.autobox2

import scala.reflect.ClassTag
import javax.swing.DefaultComboBoxModel

class AutoBoxModel[ItemType <: AutoBoxItem: ClassTag](
   c: AutoBox[ItemType])
   extends DefaultComboBoxModel[ItemType](new Array[ItemType](0)) {
   override def setSelectedItem(o: Object) {
      o match {
         case o: ItemType => c.setLastSelected(o)
         case _ =>
      }
      super.setSelectedItem(o)
   }
}