package se.culvertsoft.mgen.compiler.util

import java.io.File
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.LinkedHashMap
import scala.collection.mutable.Map
import scala.reflect.io.Path

import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.CustomCodeSection
import se.culvertsoft.mgen.api.model.GeneratedSourceFile

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
    generatedFiles: Seq[GeneratedSourceFile],
    outputPathPrepend: Option[String] = None) {

    println("Writing files to disk:")

    generatedFiles.par.foreach { generatedFile => 

      val filePath = outputPathPrepend match {
        case Some(prepend) => prepend + File.separator + generatedFile.filePath
        case _ => generatedFile.filePath
      }

      if (!FileUtils.exists(filePath)) {
        println(s"  writing: ${filePath}")
        val dir = FileUtils.directoryOf(filePath)
        Path(dir).createDirectory(true, false)
        writeToFile(filePath, generatedFile.sourceCode)
      } else {

        val sourceOnDisk = FileUtils.readToString(filePath, charset)

        val sourceToWrite =

          if (!generatedFile.hasCustomCodeSections) {
            generatedFile.sourceCode
          } else {

            findCustomCode(filePath, sourceOnDisk, generatedFile) match {
              case customSources if (customSources.isEmpty) => generatedFile.sourceCode
              case customSources => buildNewSourceCode(sourceOnDisk, generatedFile.sourceCode, customSources)
            }

          }

        if (sourceToWrite != sourceOnDisk) {
          println(s"  writing: ${filePath}")
          writeToFile(filePath, sourceToWrite)
        } else {
          println(s"  skipping (no change): ${filePath}")
        }

      }

    }

    println("")

  }

  private case class CustomSourceIndices(begin: Int, end: Int)

  private def buildNewSourceCode(
    sourceOnDisk: String,
    generatedCode: String,
    customSources: Map[CustomCodeSection, CustomSourceIndices]): String = {

    val buffer = SourceCodeBuffer.getThreadLocal()
    buffer.clear

    var readOffset = 0
    for ((section, customSourceIndices) <- customSources) {
      val insertIndex = generatedCode.indexOf(section.getEndKey, readOffset)
      buffer.backingBuffer.append(generatedCode, readOffset, insertIndex)
      buffer.backingBuffer.append(sourceOnDisk, customSourceIndices.begin, customSourceIndices.end)
      buffer.backingBuffer.append(section.getEndKey)
      readOffset = insertIndex + section.getEndKey.length
    }

    buffer.backingBuffer.append(generatedCode, readOffset, generatedCode.length)
    buffer.toString
  }

  private def findCustomCode(
    filePath: String,
    sourceOnDisk: String,
    generatedSourceFile: GeneratedSourceFile): Map[CustomCodeSection, CustomSourceIndices] = {

    val out = new LinkedHashMap[CustomCodeSection, CustomSourceIndices]();
    {
      var startSearchAt = 0
      for (section <- generatedSourceFile.customCodeSections) {
        val iBeginTagStart = sourceOnDisk.indexOf(section.getBeginKey, startSearchAt)
        if (iBeginTagStart != -1) {
          val iCustomCodeStart = iBeginTagStart + section.getBeginKey.length
          val iCustomCodeEnd = sourceOnDisk.indexOf(section.getEndKey, iCustomCodeStart)
          if (iCustomCodeEnd != -1) {
            startSearchAt = iCustomCodeEnd + section.getEndKey.length
            if (iCustomCodeStart != iCustomCodeEnd) {
              out.put(section, CustomSourceIndices(iCustomCodeStart, iCustomCodeEnd))
            }
          } else {
            throw new GenerationException(
              s"Custom code section ${section} in file ${filePath} had start but no end. Check your code.")
          }
        }
      }
    }
    out

  }

  private def writeToFile(filePath: String, code: String) {
    Files.write(Paths.get(filePath), code.getBytes(charset));
  }

}