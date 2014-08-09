package se.culvertsoft.mgen.compiler.util

import scala.collection.JavaConverters.mapAsScalaMapConverter

object SettingsUtils {

  implicit class RichSettings(_settings: java.util.Map[String, String]) {
    val settings = _settings.asScala

    def getBool(name: String): Option[Boolean] = {
      settings.get(name) match {
        case x @ Some(_) => x.map(_.toBoolean)
        case _ => None
      }
    }

  }

}