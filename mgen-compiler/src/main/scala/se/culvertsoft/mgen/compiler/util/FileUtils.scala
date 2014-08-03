package se.culvertsoft.mgen.compiler.util

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.Charset
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import scala.reflect.io.Path

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

  def writeIfChanged(
      outputs: Seq[GeneratedSourceFile], 
      outputPathPrepend: Option[String] = None) {

    println("Writing files to disk:")

    for (output <- outputs) {

      val filePath = outputPathPrepend match {
        case Some(prepend) => prepend + File.separator + output.filePath
        case _ => output.filePath
      }

      val file = new File(filePath)
      if (!file.exists || FileUtils.readToString(filePath, charset) != output.sourceCode) {
        println(s"  writing: ${filePath}")
        if (!file.exists) {
          val dir = FileUtils.directoryOf(filePath)
          Path(dir).createDirectory(true, false)
        }
        Files.write(Paths.get(filePath), output.sourceCode.getBytes(charset));
      } else {
        println(s"  skipping (no change): ${filePath}")
      }

    }

    println("")

  }
  
}