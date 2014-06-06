package se.culvertsoft.mgen.compiler.internal

import se.culvertsoft.mgen.api.plugins.Generator

object BuiltInGenerator {
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
}

abstract class BuiltInGenerator extends Generator {
   def upFirst(txt: String): String = {
      BuiltInGenerator.upFirst(txt)
   }
}