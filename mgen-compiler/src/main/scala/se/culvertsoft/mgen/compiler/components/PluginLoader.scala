package se.culvertsoft.mgen.compiler.components

import java.io.File
import java.net.URL
import java.net.URLClassLoader

import scala.Array.canBuildFrom
import scala.Option.option2Iterable
import scala.collection.mutable.HashMap
import scala.collection.mutable.TreeSet
import scala.util.Properties

import se.culvertsoft.mgen.api.exceptions.MGenException
import se.culvertsoft.mgen.compiler.util.EnvVarUtils
import se.culvertsoft.mgen.compiler.util.SplitCommaSeparated

class PluginLoader(pluginPaths_in: Seq[String], useEnvPaths: Boolean) {
  def this(commaSeparatedPaths: String, useEnvPaths: Boolean) = this(SplitCommaSeparated(commaSeparatedPaths), useEnvPaths)

  private val DEFAULT_PATH = "plugins/"
  private val pluginPaths = getPaths()
  private val fileNames = pluginPaths.flatMap(listFiles(_, ".jar", false))
  private val jarUrls = fileNames.map(fileName => new URL("jar:file:" + fileName + "!/")).toArray
  private val classLoader = URLClassLoader.newInstance(jarUrls)

  private val cache = new ThreadLocal[HashMap[String, Option[AnyRef]]] {
    override def initialValue(): HashMap[String, Option[AnyRef]] = {
      new HashMap[String, Option[AnyRef]]
    }
  }

  def getThreadLocal[T <: AnyRef](name: String): Option[T] = {
    val x = cache.get().getOrElseUpdate(name, find[T](name).map(cls => cls.newInstance()))
    x.asInstanceOf[Option[T]]
  }

  def getPaths(): Seq[String] = {

    val custom = pluginPaths_in.toList.filter(_.nonEmpty)

    val out =
      if (defaultFolderExists) {
        DEFAULT_PATH :: custom
      } else {
        custom
      }

    val envPaths =
      if (useEnvPaths)
        EnvVarUtils.getCommaSeparated("MGEN_PLUGIN_PATHS") ++ Properties.envOrNone("MGEN_INSTALL_PATH").map(_ + "/jars")
      else
        Array[String]()

    (out ++ envPaths).distinct
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

  def listFiles(dir: String, ending: String, recurse: Boolean): TreeSet[String] = {
    val out = new TreeSet[String];
    def listFiles(
      directory: String,
      fileEnding: String,
      result: TreeSet[String],
      recurse: Boolean) {
      val pluginFolder = new File(directory);
      if (!pluginFolder.exists())
        return
      if (!pluginFolder.isDirectory())
        throw new MGenException("Not a directory: " + directory);
      val curFiles = pluginFolder.listFiles();
      for (file <- curFiles) {
        if (file.isDirectory()) {
          if (recurse)
            listFiles(file.getAbsolutePath(), fileEnding, result, recurse);
        } else if (file.getName().endsWith(fileEnding)) {
          result.add(file.getAbsolutePath());
        }
      }
    }
    listFiles(dir, ending, out, recurse);
    out;
  }

}
