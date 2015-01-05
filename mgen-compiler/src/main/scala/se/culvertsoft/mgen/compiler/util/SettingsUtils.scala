package se.culvertsoft.mgen.compiler.util

import scala.collection.JavaConverters.mapAsScalaMapConverter

object SettingsUtils {

  implicit class RichSettings(settings: Map[String, String]) {

    def getBool(name: String): Option[Boolean] = {
      settings.get(name) match {
        case x @ Some(_) => x.map(_.toBoolean)
        case _ => None
      }
    }

  }

}