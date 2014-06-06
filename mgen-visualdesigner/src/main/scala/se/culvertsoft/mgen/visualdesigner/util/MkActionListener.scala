package se.culvertsoft.mgen.visualdesigner.util

import java.awt.event.ActionEvent
import java.awt.event.ActionListener

object MkActionListener {
  def apply[T](f: ActionEvent => T): ActionListener = {
    new ActionListener() {
      override def actionPerformed(e: ActionEvent) {
        f(e)
      }
    }
  }
  def apply[T](f: => T): ActionListener = {
    new ActionListener() {
      override def actionPerformed(e: ActionEvent) {
        f
      }
    }
  }
}