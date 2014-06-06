package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager

class AbsoluteLayout extends LayoutManager {
   override def addLayoutComponent(name: String, comp: Component) {}
   override def removeLayoutComponent(comp: Component) {}
   override def preferredLayoutSize(parent: Container) = { minimumLayoutSize(parent) }
   override def layoutContainer(parent: Container) {}
   override def minimumLayoutSize(parent: Container): Dimension = {

      val max = new Dimension()

      for (c <- parent.getComponents()) {
         val x = c.getLocation().x;
         val y = c.getLocation().y;
         val width = c.getSize().width;
         val height = c.getSize().height;
         if (x + width > max.width)
            max.width = x + width;
         if (y + height > max.height)
            max.height = y + height;
      }

      max

   }

}
