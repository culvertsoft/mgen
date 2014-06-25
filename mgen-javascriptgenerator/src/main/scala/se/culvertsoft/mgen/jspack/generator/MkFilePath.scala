package se.culvertsoft.mgen.jspack.generator

import java.io.File

import scala.collection.JavaConversions._
object MkFilePath {

  def apply(settings: java.util.Map[String, String]) : String = {
    val path = settings getOrElse("output_path", "")
    val filename = settings getOrElse("output_filename", "mGen.js")
    println(path + File.separator + filename)
    path + File.separator + filename
  }
}