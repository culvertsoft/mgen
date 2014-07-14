package se.culvertsoft.mgen.compiler.defaultparser

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.Charset

object FileUtils {

  val charset = Charset.forName("UTF8");
  
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

  def getAbsolutePath(filePath: String): String = {
    new File(filePath).getCanonicalPath()
  }

  def directoryOf(filePath: String): String = {

    val lastIdxSlash1 = filePath.lastIndexOf("\\")
    val lastIdxSlash2 = filePath.lastIndexOf('/')

    val fNameStartIndex = math.max(lastIdxSlash1, lastIdxSlash2) + 1
    val fNameLen = filePath.length - fNameStartIndex

    var out: String = filePath.dropRight(fNameLen)

    while (out.nonEmpty && (out.last == '\\' || out.last == '/')) {
      out = out.dropRight(1)
    }

    out
  }

  def nameOf(filePath: String): String = {
    new File(filePath).getName()
  }

  def removeFileEnding(filePath: String): String = {
    filePath.split('.').dropRight(1).mkString(".")
  }

  def findFile(filePath: String, searchPaths: Seq[String]): Option[File] = {
    if (isFile(filePath)) {
      Some(new File(filePath))
    } else {
      searchPaths.map(_ + File.separatorChar + filePath).find(isFile).map(new File(_))
    }
  }

  def checkiSsFileOrThrow(filePath: String): File = {
    val file = new File(filePath)
    if (!file.exists)
      throw new RuntimeException(s"File does not exist: ${filePath}")
    if (!file.isFile)
      throw new RuntimeException(s"Path points to a directory: ${filePath}")
    file
  }

  def readToString(filePath: String, chrset: Charset = charset): String = {
    val encoded = Files.readAllBytes(Paths.get(filePath));
    return new String(encoded, chrset);
  }

}