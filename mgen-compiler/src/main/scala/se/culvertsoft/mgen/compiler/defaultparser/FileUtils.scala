package se.culvertsoft.mgen.compiler.defaultparser

import java.io.File

object FileUtils {

  def exists(filePath: String): Boolean = {
    new File(filePath).exists()
  }

  def isFile(filePath: String): Boolean = {
    new File(filePath).isFile()
  }

  def isDir(filePath: String): Boolean = {
    new File(filePath).isDirectory()
  }

  def apply(filePath: String): String = {
    new File(filePath).getParentFile().getPath()
  }

  def directoryOf(filePath: String): String = {
    new File(filePath).getParentFile().getPath()
  }
  
  def nameOf(filePath: String): String = {
    new File(filePath).getName()
  }

  def removeFileEnding(filePath: String): String = {
    filePath.split('.').dropRight(1).mkString(".")
  }

  def checkiSsFileOrThrow(filePath: String) {
    val file = new File(filePath)
    if (!file.exists)
      throw new RuntimeException(s"File does not exist: ${filePath}")
    if (!file.isFile)
      throw new RuntimeException(s"Path points to a directory: ${filePath}")
  }

  def readToString(filePath: String): String = {
    scala.io.Source.fromFile(filePath).mkString
  }

}