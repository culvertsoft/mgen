package se.culvertsoft.mgen.compiler.internal

import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
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

  def ln()(implicit txtBuffer: SuperStringBuffer): SuperStringBuffer = {
    txtBuffer.textln()
  }

  def ln(nTabs: Int, txt: String)(implicit txtBuffer: SuperStringBuffer): SuperStringBuffer = {
    txtBuffer.tabs(nTabs + txtBuffer.getTabLevel()).textln(txt)
  }

  def ln(txt: String)(implicit txtBuffer: SuperStringBuffer): SuperStringBuffer = {
    ln(0, txt)
  }

  def scope(head: String)(body: => Any)(implicit txtBuffer: SuperStringBuffer) {
    txtBuffer.tabs(txtBuffer.getTabLevel()).text(head).braceBegin().endl(){
      body
    }
    txtBuffer.tabs(txtBuffer.getTabLevel()).braceEnd().endl()
  }

  def endl()(implicit txtBuffer: SuperStringBuffer): SuperStringBuffer = {
    txtBuffer.endl()
  }

}