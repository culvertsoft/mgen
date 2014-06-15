package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Container
import java.awt.Dimension

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.visualdesigner.control.Controller
import se.culvertsoft.mgen.visualdesigner.model.CustomType

class ClassViewLayout(
   classView: ClassView,
   clas: CustomType,
   controller: Controller) extends AbsoluteLayout {

   val xFieldPad = 2
   val yFieldPad = 1
   val innerFieldHeight = 26
   val outerFieldHeight = innerFieldHeight + yFieldPad

   override def preferredLayoutSize(parent: Container) = {
      minimumLayoutSize(parent)
   }

   override def layoutContainer(parent: Container) {

      val y0 = yFieldPad + 1
      val fields = clas.getFields()
      val x = xFieldPad
      val w = math.max(40, classView.scrollpane.getWidth() - 2 * xFieldPad - 2)
      val h = innerFieldHeight

      controller.bulkOperation {
         for ((field, i) <- fields.zipWithIndex) {
            val y = y0 + i * outerFieldHeight
            val view = controller.viewMgr.view(field)
            view.setBounds(x, y, w, h)
         }
      }

   }

   override def minimumLayoutSize(parent: Container): Dimension = {
      val h = clas.getFields().size() * outerFieldHeight
      new Dimension(math.max(100, classView.scrollpane.getWidth() - 10), h)
   }

}
