package se.culvertsoft.mgen.visualdesigner.view

import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.ActionEvent
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

import Graphics2DOps.RichGraphics2D
import javax.swing.AbstractAction
import javax.swing.JPanel
import javax.swing.JTextField
import se.culvertsoft.mgen.visualdesigner.HotKey
import se.culvertsoft.mgen.visualdesigner.HotKey.toStroke

object Labeled {
   val BG_COLOR = new Color(240, 240, 240)
   val DEFAULT_PADDING = 10
   val DEFAULT_BORDER_COLOR = Bordered.DEFAULT_COLOR
   val DEFAULT_TEXT_COLOR = Color.DARK_GRAY
   val LABEL_PADDING = 10
   val LABEL_HEIGHT = 22
   val OVERFLOW_TEXT_ENDING = "..."
}

trait Labeled {
   self: AbstractView =>
   import Graphics2DOps._
   import Labeled._

   var labelTextWidth = 0
   var overflowTextEndingWidth = 0

   val labelPanel = new JPanel() {
      override def paintComponent(g: Graphics) {
         drawLabel(g.asInstanceOf[Graphics2D])
      }
   }
   addOverlayOffsetBounds(labelPanel, 0, -labelHeight(), labelWidth(), labelHeight())

   val acceptAction = new AbstractAction() {
      override def actionPerformed(e: ActionEvent) {
         acceptRename()
      }
   }

   val cancelAction = new AbstractAction() {
      override def actionPerformed(e: ActionEvent) {
         cancelRename()
      }
   }

   val renameTextField = new JTextField
   renameTextField.getInputMap().put(HotKey(KeyEvent.VK_ENTER), "accept")
   renameTextField.getInputMap().put(HotKey(KeyEvent.VK_ESCAPE), "cancel")
   renameTextField.getActionMap().put("accept", acceptAction)
   renameTextField.getActionMap().put("cancel", cancelAction)
   val renameDblClickMouseListener = new MouseAdapter() {
      override def mouseClicked(e: MouseEvent) {
         if (e.getClickCount() == 2 && renameTextField.getParent() == null) {
            initRename()
         }
      }
   }
   
   renameTextField.addFocusListener(new FocusAdapter() {
      override def focusLost(e: FocusEvent) {
         acceptRename()
      }
   })

   def initRename() {
      renameTextField.setText(self.entity.getName())
      renameTextField.setSelectionStart(0)
      renameTextField.setSelectionEnd(self.entity.getName().length())
      renameTextField.setPreferredSize(labelPanel.getSize())
      labelPanel.add(renameTextField)
      labelPanel.validate()
      labelPanel.repaint()
      renameTextField.requestFocusInWindow()
   }

   def cancelRename() {
      labelPanel.remove(renameTextField)
      labelPanel.repaint()
   }

   def acceptRename() {
      if (renameTextField.getParent() != null) {
         labelPanel.remove(renameTextField)
         labelPanel.repaint()
         controller.rename(entity, renameTextField.getText())
      }
   }

   labelPanel.addMouseListener(renameDblClickMouseListener)

   def getLabelTextWidth(g: Graphics2D, label: String = labelText): Int = g.getFontMetrics().stringWidth(label)
   def getLabelTextHeight(g: Graphics2D): Int = g.getFontMetrics().getHeight()

   def labelHeight(): Int = LABEL_HEIGHT
   def labelPadding(): Int = LABEL_HEIGHT
   def labelText(): String

   def labelTextColor(): Color = { DEFAULT_TEXT_COLOR }
   def labelBackgroundColor(): Color = { BG_COLOR }
   def labelBorderColor(): Color = {
      this match {
         case _this: Selectable if (_this.isSelected()) => Selectable.DEFAULT_SELECTED_BORDER_COLOR
         case _this: Selectable if (_this.isHovered()) => Selectable.DEFAULT_HOVERED_BORDER_COLOR
         case _ => Labeled.DEFAULT_BORDER_COLOR
      }
   }

   def labelWidth(s: Int = labelTextWidth): Int = {
      return Math.min(s + DEFAULT_PADDING, self.width())
   }

   def drawLabel(g: Graphics2D) {
      if (labelTextWidth != getLabelTextWidth(g, labelText)) {
         labelTextWidth = getLabelTextWidth(g, labelText)
         labelPanel.setSize(labelWidth(), labelPanel.getHeight())
         overflowTextEndingWidth = getLabelTextWidth(g, OVERFLOW_TEXT_ENDING)
      }

      val width = labelWidth()
      val height = labelHeight()

      g.color(labelBackgroundColor()) {
         g.fillRoundRect(0, 0, width - 1, height, 5, 5);
      }

      g.color(labelBorderColor()) {
         g.drawRoundRect(0, 0, width - 1, height, 5, 5);
      }

      drawLabelText(g)

   }

   def drawLabelText(g: Graphics2D) {

      val width = labelWidth()
      val height = labelHeight()

      g.color(labelTextColor()) {
         if (labelTextWidth + DEFAULT_PADDING <= width) {
            g.drawString(labelText, (width - labelTextWidth) / 2, height - (height - getLabelTextHeight(g)) / 2 - 2)
         } else {
            var textWidth = labelTextWidth
            var text = labelText
            var removedLetters = 1
            while (textWidth > self.width() && removedLetters != labelText.size) {
               text = labelText.substring(0, labelText.size - removedLetters) + OVERFLOW_TEXT_ENDING
               textWidth = getLabelTextWidth(g, text)
               removedLetters += 1
            }
            g.drawString(text, DEFAULT_PADDING / 2, height - (height - getLabelTextHeight(g)) / 2 - 2)
         }
      }
   }

}
