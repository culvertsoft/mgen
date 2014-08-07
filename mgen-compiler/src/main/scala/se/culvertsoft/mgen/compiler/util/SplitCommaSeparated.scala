package se.culvertsoft.mgen.compiler.util

import scala.Array.fallbackCanBuildFrom

object SplitCommaSeparated {
  def apply(text: String): Seq[String] = {
    text.split(",").map(_.trim())
  }
}