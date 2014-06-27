package se.culvertsoft.mgen.compiler.internal

import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

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

  def txt(txt: String)(implicit b: SuperStringBuffer): SuperStringBuffer = {
    b.text(txt)
  }

  def txt(nTabs: Int, txt: String)(implicit b: SuperStringBuffer): SuperStringBuffer = {
    b.tabs(nTabs).text(txt)
  }

  def ln()(implicit b: SuperStringBuffer): SuperStringBuffer = {
    b.textln()
  }

  def ln(nTabs: Int, txt: String)(implicit b: SuperStringBuffer): SuperStringBuffer = {
    b.tabs(nTabs + b.tabLevel).textln(txt)
  }

  def ln(txt: String)(implicit b: SuperStringBuffer): SuperStringBuffer = {
    ln(0, txt)
  }

  def scope(head: String)(body: => Any)(implicit b: SuperStringBuffer) {
    b.tabs(b.tabLevel).text(head).text(b.scopeBegin).endl() {
      body
    }
    b.tabs(b.tabLevel).text(b.scopeEnd).endl()
  }

  def endl()(implicit b: SuperStringBuffer): SuperStringBuffer = {
    b.endl()
  }

}
