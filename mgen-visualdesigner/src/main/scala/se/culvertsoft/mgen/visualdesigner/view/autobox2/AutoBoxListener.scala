package se.culvertsoft.mgen.visualdesigner.view.autobox2

import java.awt.event.FocusEvent
import scala.reflect.ClassTag

class AutoBoxListener[ItemType <: AutoBoxItem: ClassTag] {
  def finishedItem(t: ItemType) {}
  def requestFocusUp() {}
  def focusGained(e: FocusEvent) {}
}