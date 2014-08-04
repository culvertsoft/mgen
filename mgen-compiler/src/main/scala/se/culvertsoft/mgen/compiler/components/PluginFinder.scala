package se.culvertsoft.mgen.compiler.components

import java.io.File
import java.net.URL
import java.net.URLClassLoader

import scala.collection.JavaConversions.asScalaSet
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.util.internal.ListFiles

class PluginFinder(pluginPaths_in: Seq[String]) {

  val DEFAULT_PATH = "plugins/"
  val pluginPaths = getPaths()
  val fileNames = pluginPaths.flatMap(path => ListFiles.list(path, ".jar", false))
  val jarUrls = fileNames.map(fileName => new URL("jar:file:" + fileName + "!/")).toArray
  val classLoader = URLClassLoader.newInstance(jarUrls)

  val cache = new ThreadLocal[HashMap[String, Option[AnyRef]]] {
    override def initialValue(): HashMap[String, Option[AnyRef]] = {
      new HashMap[String, Option[AnyRef]]
    }
  }

  def getCached[T <: AnyRef](name: String): Option[T] = {
    val x = cache.get().getOrElseUpdate(name, find[T](name).map(cls => cls.newInstance()))
    x.asInstanceOf[Option[T]]
  }

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

  def find[T](classpath: String): Option[Class[T]] = {
    try {
      Some(Class.forName(classpath).asInstanceOf[Class[T]])
    } catch {
      case e: ClassNotFoundException =>
        try {
          val cls = Class.forName(classpath, false, classLoader)
          Some(cls.asInstanceOf[Class[T]])
        } catch {
          case e: ClassNotFoundException =>
            None
        }
    }

  }

}
