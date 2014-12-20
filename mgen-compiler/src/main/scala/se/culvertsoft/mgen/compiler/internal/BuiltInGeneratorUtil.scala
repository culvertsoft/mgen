package se.culvertsoft.mgen.compiler.internal

import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object BuiltInGeneratorUtil {

  def upFirst(txt: String): String = {
    if (txt.size == 1) {
      txt.toUpperCase()
    } else {
      txt.charAt(0).toUpper + txt.substring(1)
    }
  }

  def quote(txt: String): String = {
    '"' + txt + '"'
  }

  def txt(txt: String)(implicit b: SourceCodeBuffer): SourceCodeBuffer = {
    b.text(txt)
  }

  def txt(nTabs: Int, txt: String)(implicit b: SourceCodeBuffer): SourceCodeBuffer = {
    b.tabs(nTabs).text(txt)
  }

  def ln()(implicit b: SourceCodeBuffer): SourceCodeBuffer = {
    b.textln()
  }

  def ln(nTabs: Int, txt: String)(implicit b: SourceCodeBuffer): SourceCodeBuffer = {
    b.tabs(nTabs + b.tabLevel).textln(txt)
  }

  def ln(txt: String)(implicit b: SourceCodeBuffer): SourceCodeBuffer = {
    ln(0, txt)
  }

  def scope(head: String)(body: => Any)(implicit b: SourceCodeBuffer) {
    b.tabs(b.tabLevel).text(head).text(b.scopeBegin).endl() {
      body
    }
    b.tabs(b.tabLevel).text(b.scopeEnd).endl()
  }

  def scopeExt(head: String, extraEnd: String)(body: => Any)(implicit b: SourceCodeBuffer) {
    b.tabs(b.tabLevel).text(head).text(b.scopeBegin).endl() {
      body
    }
    b.tabs(b.tabLevel).text(b.scopeEnd + extraEnd).endl()
  }

  def endl()(implicit b: SourceCodeBuffer): SourceCodeBuffer = {
    b.endl()
  }

}
