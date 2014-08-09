package se.culvertsoft.mgen.compiler.util

import scala.Array.canBuildFrom
import scala.util.Properties

object EnvVarUtils {

  def getSeparated(name: String, separator: String): Array[String] = {
    Properties
      .envOrNone(name)
      .map(_.split(separator))
      .getOrElse(Array.empty)
      .map(_.trim)
      .filter(_.nonEmpty)
      .distinct
  }

  def getCommaSeparated(name: String): Array[String] = {
    getSeparated(name, ",")
  }

}