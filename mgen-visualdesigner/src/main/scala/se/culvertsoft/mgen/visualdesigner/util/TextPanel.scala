package se.culvertsoft.mgen.visualdesigner.util

import java.awt.BorderLayout
import java.awt.Color
import java.awt.Insets

import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextPane
import javax.swing.border.EmptyBorder
import javax.swing.text.BadLocationException
import javax.swing.text.DefaultCaret
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants
import javax.swing.text.StyleContext

class TextPanel extends JPanel with ConsoleTarget {

  val textPane = new JTextPane;
  val scrollPane = new JScrollPane(textPane);
  val eb = new EmptyBorder(new Insets(10, 10, 10, 10));

  setLayout(new BorderLayout());
  textPane.setBorder(eb);
  textPane.setMargin(new Insets(5, 5, 5, 5));
  textPane.setEditable(false);
  add(scrollPane);

  /**
   * *********************************************
   *
   *
   * 				METHODS
   *
   * ********************************************
   */

  def toFront() {

  }

  def append(msg: String, c: Color) {
    val sc = StyleContext.getDefaultStyleContext();
    val aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
    val len = textPane.getDocument().getLength();

    textPane.setCaretPosition(len);
    textPane.setCharacterAttributes(aset, false);

    try {
      textPane.getDocument().insertString(getLength(), msg, aset);
    } catch {
      case e: BadLocationException =>
      // Can't print this since we would get feedback out stdout(stackoverflow)
    }

    val caret = textPane.getCaret().asInstanceOf[DefaultCaret];
    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

  }

  def getLength(): Int = {
    textPane.getDocument().getLength()
  }

}