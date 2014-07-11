package se.culvertsoft.mgen.compiler.util

import scala.language.implicitConversions

object SuperStringBuffer {
  implicit def SuperStringBuffer2String(s: SuperStringBuffer) = s.toString
}

class SuperStringBuffer(
  val scopeBegin: String = " {",
  val scopeEnd: String = "}") {

  private var tabString = "\t"
  private val buffer = new StringBuilder
  private var _tabLevel = 0

  def tabLevel() = { _tabLevel }
  def setTabLevel(t: Int) = { _tabLevel = t }

  def tabs(n: Int) = {
    for (i <- 0 until n)
      buffer.append(tabString)
    this
  }

  def endl(): SuperStringBuffer = {
    buffer.append('\n')
    this
  }

  def setTabString(s: String) = { tabString = s }

  def endl2() = { buffer.append('\n').append('\n'); this }
  def text(s: String) = { buffer.append(s); this }
  def +=(s: String): SuperStringBuffer = { this.text(s) }
  def textln(s: String = "") = { buffer.append(s).append('\n'); this }
  def function(name: String)(params: String = "") = { buffer.append(name).append('(').append(params).append(')'); this }
  def paranthBegin() = { buffer.append('('); this }
  def paranthEnd() = { buffer.append(')'); this }
  def braceBegin() = { buffer.append('{'); this }
  def braceEnd() = { buffer.append('}'); this }
  def comma() = { buffer.append(','); this }
  def commaSpace() = { buffer.append(", "); this }
  def tag(inside: String) = { text(s"<$inside>"); this }
  def tagEnd(inside: String) = { tag("/" + inside); this }
  def clear() = { buffer.setLength(0); this }
  def char(c: Char) = { buffer.append(c); this }
  def size() = buffer.length()
  def length() = size()
  def removeLast(size: Int) = { buffer.setLength(buffer.length() - size); this }
  def apply[A](f: => A) {
    try {
      _tabLevel += 1
      f
    } finally {
      _tabLevel -= 1
    }
  }
  override def toString = buffer.toString

}
