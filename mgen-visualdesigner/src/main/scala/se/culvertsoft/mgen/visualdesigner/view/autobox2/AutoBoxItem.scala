package se.culvertsoft.mgen.visualdesigner.view.autobox2

trait AutoBoxItem {

  def deepCopy(): AutoBoxItem

  def isComplete(): Boolean

  def tooltipString(): String = toString()

  def removeLast(): AutoBoxItem {}

  def completeWith(t: AutoBoxItem): AutoBoxItem {}

  def matches(s: String): Boolean = toString == s
  
  def idString(): String

}
