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
  
  def ln(nTabs: Int, txt: String)(implicit txtBuffer: SuperStringBuffer): SuperStringBuffer = {
    txtBuffer.tabs(nTabs).textln(txt)
  }

}