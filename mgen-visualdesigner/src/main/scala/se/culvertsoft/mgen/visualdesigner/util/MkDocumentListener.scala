package se.culvertsoft.mgen.visualdesigner.util

import javax.swing.event.DocumentListener
import javax.swing.event.DocumentEvent

object MkDocumentListener {
  def apply[T](f: => T): DocumentListener = {
    new DocumentListener() {
      override def changedUpdate(d: DocumentEvent) { f }
      override def insertUpdate(d: DocumentEvent) { f }
      override def removeUpdate(d: DocumentEvent) { f }
    }
  }
  def apply[T](f: DocumentEvent => T): DocumentListener = {
    new DocumentListener() {
      override def changedUpdate(d: DocumentEvent) { f(d) }
      override def insertUpdate(d: DocumentEvent) { f(d) }
      override def removeUpdate(d: DocumentEvent) { f(d) }
    }
  }
}