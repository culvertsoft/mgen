package se.culvertsoft.mgen.visualdesigner.view.autobox2

import java.awt.Component

import javax.swing.DefaultListCellRenderer
import javax.swing.JComponent
import javax.swing.JList
import javax.swing.ListCellRenderer

class AutoBoxRenderer extends ListCellRenderer[Object] {

   private val delegate: ListCellRenderer[Object] = new DefaultListCellRenderer

   override def getListCellRendererComponent(
      list: JList[_ <: Object],
      value: Object,
      index: Int,
      isSelected: Boolean,
      cellHasFocus: Boolean): Component = {

      val component = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus).asInstanceOf[JComponent]

      value match {
         case t: AutoBoxItem =>
            val desiredText = t.tooltipString()
            if (list.getToolTipText() != desiredText) {
               list.setToolTipText(desiredText)
            }
         case _ =>
      }

      return component;
   }

}