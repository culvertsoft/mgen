package se.culvertsoft.mgen.compiler.plugins

import java.io.File
import java.net.URL
import java.net.URLClassLoader

import scala.collection.JavaConversions.asScalaSet

import se.culvertsoft.mgen.api.util.internal.ListFiles

class PluginFinder(pluginPaths_in: Seq[String]) {

  val DEFAULT_PATH = "plugins/"
  val pluginPaths = getPaths()
  val fileNames = pluginPaths.flatMap(path => ListFiles.list(path, ".jar", false))
  val jarUrls = fileNames.map(fileName => new URL("jar:file:" + fileName + "!/")).toArray
  val classLoader = URLClassLoader.newInstance(jarUrls)

  def getPaths(): Seq[String] = {
    val custom = pluginPaths_in.toList.filter(_.nonEmpty)
    if (defaultFolderExists) {
      DEFAULT_PATH :: custom
    } else {
      custom
    }
  }

  def defaultFolderExists(): Boolean = {
    val f = new File(DEFAULT_PATH)
    f.exists() && f.isDirectory()
  }

  def find[T](classpath: String): Option[Class[_ <: T]] = {
    try {
      Some(Class.forName(classpath).asInstanceOf[Class[_ <: T]])
    } catch {
      case e: ClassNotFoundException =>
        try {
          val cls = Class.forName(classpath, false, classLoader)
          Some(cls.asInstanceOf[Class[_ <: T]])
        } catch {
          case e: ClassNotFoundException =>
            None
        }
    }

  }

}
