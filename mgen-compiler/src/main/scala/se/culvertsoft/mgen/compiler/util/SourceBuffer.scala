package se.culvertsoft.mgen.compiler.util

private class InternBuffer {
  private val buffer = new StringBuilder
  protected[util] def append(s: String): this.type = {
    buffer.append(s)
    this
  }
  protected[util] def finish(): String = {
    val out = buffer.toString()
    buffer.setLength(0)
    out
  }
  protected[util] def nonEmpty(): Boolean = {
    buffer.length() != 0
  }
}

class SourceBuffer(
  val scopeBegin: String = " {",
  val scopeEnd: String = "}") {
  import SourceBuffer._

  private val lineBuffer = new InternBuffer
  private val fullBuffer = new InternBuffer

  private var indentationLevel = 0

  private def finishLine() {
    for (i <- 0 until indentationLevel)
      fullBuffer.append(INDENT)
    fullBuffer.append(lineBuffer.finish()).append(LINE_SEPARATOR)
  }

  protected[util] def incIndent(n: Int) {
    setIndent(indentationLevel + n)
  }

  protected[util] def decIndent(n: Int) {
    setIndent(indentationLevel - n)
  }

  protected[util] def setIndent(n: Int) {
    indentationLevel = n
  }

  protected[util] def doIndented[A](n: Int, f: => A) {
    try {
      incIndent(n)
      f
    } finally {
      decIndent(n)
    }
  }

  def ln(s: String): this.type = {
    lineBuffer.append(s)
    finishLine()
    this
  }

  def indent[A](s: String, n: Int = 1)(f: => A): SourceBuffer = {
    ln(s)
    doIndented(n, f)
    this
  }

  def endl(): SourceBuffer = {
    finishLine()
    this
  }

  def txt(s: String): this.type = {
    lineBuffer.append(s)
    this
  }

  def reset(): SourceBuffer = {
    finish()
    setIndent(0)
    this
  }

  def scope[A](s: String)(f: => A): SourceBuffer = {
    indent[A](s + scopeBegin)(f)
    ln(scopeEnd)
    this
  }

  def finish(): String = {
    if (lineBuffer.nonEmpty)
      finishLine()
    fullBuffer.finish()
  }

  def +=(s: String): SourceBuffer = {
    ln(s)
  }

}

object SourceBuffer {

  val LINE_SEPARATOR = System.lineSeparator
  val INDENT = "    "

  def txt(s: String)(implicit code: SourceBuffer): SourceBuffer = {
    code.txt(s)
  }

  def ln(s: String)(implicit code: SourceBuffer): SourceBuffer = {
    code.ln(s)
  }

  def endl()(implicit code: SourceBuffer): SourceBuffer = {
    code.endl()
  }

  def indent[A](s: String, n: Int = 1)(f: => A)(implicit code: SourceBuffer): SourceBuffer = {
    code.indent(s, n)(f)
  }

  def scope[A](s: String)(f: => A)(implicit code: SourceBuffer): SourceBuffer = {
    code.scope(s)(f)
  }

}
