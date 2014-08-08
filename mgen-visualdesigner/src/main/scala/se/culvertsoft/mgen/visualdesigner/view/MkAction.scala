package se.culvertsoft.mgen.visualdesigner.view

import java.awt.event.ActionEvent

import javax.swing.AbstractAction
import javax.swing.Action
import javax.swing.ImageIcon
import javax.swing.KeyStroke

object MkAction {

  def apply(
    name: String,
    icon: ImageIcon,
    hotkey: KeyStroke,
    tooltip: String)(f: => Unit): AbstractAction = {

    new AbstractAction(name) {
      if (icon != null) { putValue(Action.SMALL_ICON, icon) }
      if (tooltip != null && tooltip.nonEmpty) { putValue(Action.SHORT_DESCRIPTION, tooltip); } else { putValue(Action.SHORT_DESCRIPTION, name); }
      if (hotkey != null) { putValue(Action.ACCELERATOR_KEY, hotkey); }
      override def actionPerformed(e: ActionEvent) {
        f
      }
    }

  }

  def apply(
    name: String,
    icon: ImageIcon)(f: => Unit): AbstractAction = {
    apply(name, icon, "")(f)
  }

  def apply(
    name: String,
    icon: ImageIcon,
    tooltip: String)(f: => Unit): AbstractAction = {
    apply(name, icon, null, tooltip)(f)
  }

  def apply(
    name: String,
    icon: ImageIcon,
    hotKey: KeyStroke)(f: => Unit): AbstractAction = {
    apply(name, icon, hotKey, "")(f)
  }

  def apply(
    name: String,
    hotKey: KeyStroke,
    tooltip: String)(f: => Unit): AbstractAction = {
    apply(name, null, hotKey, "")(f)
  }

  def apply(
    name: String,
    hotKey: KeyStroke)(f: => Unit): AbstractAction = {
    apply(name, null, hotKey, "")(f)
  }

  def apply(f: => Unit): AbstractAction = {
    apply("", null, null, "")(f)
  }

}