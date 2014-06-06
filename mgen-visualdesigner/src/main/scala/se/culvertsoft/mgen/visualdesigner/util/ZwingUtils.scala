package se.culvertsoft.mgen.visualdesigner.util

import java.awt.Component
import java.awt.event.KeyEvent

import javax.swing.SwingUtilities

object ZwingUtils {

  def hasAncestor(c: Component, potentialAncestor: Component): Boolean = {
    SwingUtilities.isDescendingFrom(c, potentialAncestor)
  }

  def originatesFromChild(e: KeyEvent, potentialAncestor: Component): Boolean = {
    e.getSource() match {
      case c: Component => hasAncestor(c, potentialAncestor)
      case _ => false
    }
  }

}
