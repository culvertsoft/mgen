package se.culvertsoft.mgen.compiler

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.components.GenerateCode
import se.culvertsoft.mgen.compiler.components.ParseCmdLineArgs
import se.culvertsoft.mgen.compiler.internal.PrintHelp
import se.culvertsoft.mgen.compiler.internal.PrintIntro
import se.culvertsoft.mgen.compiler.util.FileUtils

object MGenCompiler {

  def main(cmdLineArgs: Array[String]) {

    val result = Try {

      printIntro()

      val settings = parseCmdLineSettings(cmdLineArgs)

      if (settings.isEmpty)
        return

      val code = generateCode(settings)

      writeToDisk(code, settings)

    }

    result match {
      case Success(_) => handleSuccess()
      case Failure(err) => handleFailure(err)
    }

  }

  def printIntro() {
    PrintIntro(s"(${BuildVersion.GIT_TAG} ${BuildVersion.GIT_COMMIT_DATE})")
  }

  def generateCode(settings: Map[String, String]): Seq[GeneratedSourceFile] = {
    GenerateCode(settings)
  }

  def parseCmdLineSettings(cmdLineArgs: Array[String]): Map[String, String] = {
    ParseCmdLineArgs(cmdLineArgs)
  }

  def writeToDisk(code: Seq[GeneratedSourceFile], settings: Map[String, String]) {
    FileUtils.writeIfChanged(code, settings.get("output_path"))
  }

  def handleSuccess() {
    println("*** COMPILATION SUCCESS ***")
  }

  def handleFailure(err: Throwable) {
    println
    println(s"*** COMPILATION FAILED (${err.getMessage}), see error log below ***")
    println
    PrintHelp()
    println
    System.err.flush()
    System.out.flush()
    throw err
  }

}