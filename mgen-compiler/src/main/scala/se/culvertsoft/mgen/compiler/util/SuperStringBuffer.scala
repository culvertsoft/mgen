package se.culvertsoft.mgen.compiler.util

object SuperStringBuffer {
  implicit def SuperStringBuffer2String(s : SuperStringBuffer) = s.toString
}

class SuperStringBuffer {

  private var TAB_STRING = "    "
  private val buffer = new StringBuffer
  private var tabLevel = 0

  def getTabLevel() = { tabLevel }
  def setTabLevel(t: Int) = { tabLevel = t }

  def tabs(n: Int) = {
    for (i <- 0 until n)
      buffer.append(TAB_STRING)
    this
  }

  def endl(): SuperStringBuffer = {
    buffer.append('\n')
    m_firstOnLine = true
    this
  }

  def setTabString(s: String) = { TAB_STRING = s }

  def endl2() = { buffer.append('\n').append('\n'); this }
  def text(s: String) = { buffer.append(s); this }
  def +=(s: String) : SuperStringBuffer = { this.text(s) }
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
  def apply(f: => Unit) {
    tabLevel += 1
    try {
      f
    } finally {
      tabLevel -= 1
    }
  }
  override def toString = buffer.toString


  //private var m_tabs = 0
  private var m_firstOnLine = false
  /*
  def write(t: String): SuperStringBuffer = {
     this << t
  }

  def <<(t: String): SuperStringBuffer = {
     if (isFirstOnLine)
        tabs(m_tabs)
     text(t)
     m_firstOnLine = false
     this
  }

  def <<(cg: SuperStringBuffer): SuperStringBuffer = {
     this
  }

  def line(t: String): SuperStringBuffer = {
     this << t << endl
  }

  def indentInc(): SuperStringBuffer = {
     m_tabs += 1
     this
  }

  def indentDec(): SuperStringBuffer = {
     m_tabs -= 1
     this
  }

  def apply(f: => String): SuperStringBuffer = {
     indentInc << f << indentDec
  }

  def isFirstOnLine(): Boolean = {
     m_firstOnLine
  }
*/

}
