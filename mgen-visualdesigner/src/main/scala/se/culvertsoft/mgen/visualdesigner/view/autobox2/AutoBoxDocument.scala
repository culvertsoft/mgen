package se.culvertsoft.mgen.visualdesigner.view.autobox2

import java.awt.Toolkit

import scala.reflect.ClassTag

import javax.swing.DefaultComboBoxModel
import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.PlainDocument

class AutoBoxDocument[ItemType <: AutoBoxItem: ClassTag](
  c: AutoBox[ItemType],
  cmpModel: DefaultComboBoxModel[ItemType])
  extends PlainDocument {

  @throws[BadLocationException]
  override def insertString(offs: Int, str: String, a: AttributeSet) {

    c.safe {

      if (c.isInputInhibited || !c.isPopupVisible())
        return

      val click_continuing =
        offs == 0 &&
          str.size > 1 //** &&
      /*c.build != null &&*/
      //  !c.isComplete()

      if (click_continuing) {
        c.inhibitInput {
          if (c.build != null) {
            cmpModel.setSelectedItem(c.lastSelected)
            super.insertString(0, c.build.deepCopy().completeWith(c.lastSelected).toString(), null)
            return
          } else {
            cmpModel.setSelectedItem(c.lastSelected)
            super.insertString(0, c.lastSelected.toString(), null)
            return
          }
        }
      }

      val userTextBefore = getText(0, getLength())
      // val selStartBefore = c.cmpEditor.getSelectionStart()
      // val selEndBefore = c.cmpEditor.getSelectionEnd()

      super.insertString(offs, str, a)

      // Check how much of "build" that we can keep,
      // Remove the rest of the text AND of build
      while (c.build != null && !c.build.matches(userTextBefore)) {
        c.setBuild(c.build.removeLast().asInstanceOf[ItemType])
      }

      val prependString = if (c.build != null) c.build.toString() else ""
      val testString = getText(prependString.length, getLength() - prependString.length)
      val item = lookupItem(testString)

      if (item != null) {

        val preview =
          if (c.build != null) c.build.deepCopy().completeWith(item.deepCopy())
          else item.deepCopy()

        c.inhibitInput {
          cmpModel.setSelectedItem(item.deepCopy())
          setText(preview.toString())
          val highlightFrom = offs + str.length()
          highlightCompletedText(highlightFrom)
          if (highlightFrom == getLength()) {
            c.setPopupVisible(false)
          }
        }

      } else {
        Toolkit.getDefaultToolkit().beep()
        c.inhibitInput {
          setText(userTextBefore)
        }
      }

    }

  }

  def lookupItem(pattern: String): ItemType = {

    val n = cmpModel.getSize()
    for (i <- 0 until n) {
      val anItem = cmpModel.getElementAt(i)
      if (startsWithIgnoreCase(anItem.toString(), pattern)) {
        return anItem
      }
    }

    return null.asInstanceOf[ItemType]
  }

  // checks if str1 starts with str2 - ignores case
  def startsWithIgnoreCase(str1: String, str2: String): Boolean = {
    return str1.toUpperCase().startsWith(str2.toUpperCase())
  }

  @throws[BadLocationException]
  override def remove(a: Int, b: Int) {
    if (!c.isPopupVisible())
      return
    super.remove(a, b)
  }

  def revert() {
    if (c.build != null) {
      setText(c.build.toString())
    } else {
      c.selected() match {
        case Some(s) =>
          c.setBuild(s.deepCopy().asInstanceOf[ItemType])
          setText(s.toString())
        case None =>
          c.setBuild(c.finished.deepCopy().asInstanceOf[ItemType])
          setText(c.finished.toString())
      }
    }
  }

  def setText(text: String) {
    super.remove(0, getLength())
    super.insertString(0, text, null)
  }

  def highlightCompletedText(start: Int) {
    c.cmpEditor.setCaretPosition(getLength())
    c.cmpEditor.moveCaretPosition(start)
  }
}