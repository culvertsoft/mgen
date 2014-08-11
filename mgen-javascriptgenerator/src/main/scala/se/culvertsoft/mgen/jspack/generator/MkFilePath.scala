package se.culvertsoft.mgen.jspack.generator

import java.io.File

import scala.collection.JavaConversions.mapAsScalaMap
object MkFilePath {

  def apply(settings: java.util.Map[String, String]): String = {
    val path = settings getOrElse ("output_path", "")
    val filename = settings getOrElse ("output_filename", "mGen.js")
    path + File.separator + filename
  }
}