package se.culvertsoft.mgen.compiler

import scala.Array.canBuildFrom
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.components.GenerateCode
import se.culvertsoft.mgen.compiler.internal.PrintHelp
import se.culvertsoft.mgen.compiler.internal.PrintIntro
import se.culvertsoft.mgen.compiler.util.FileUtils
import se.culvertsoft.mgen.compiler.util.ParseKeyValuePairs

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
      case Success(_) =>
        println("*** COMPILATION SUCCESS ***")
      case Failure(err) =>
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

  def printIntro() {
    PrintIntro(s"(${BuildVersion.GIT_TAG} ${BuildVersion.GIT_COMMIT_DATE})")
  }

  def generateCode(settings: Map[String, String]): Seq[GeneratedSourceFile] = {
    GenerateCode(settings)
  }

  def parseCmdLineSettings(cmdLineArgs: Array[String]): Map[String, String] = {

    // Parse input parameters
    val params_raw = cmdLineArgs.map(_.trim()).toBuffer
    if (params_raw.isEmpty || params_raw.contains("-help")) {
      PrintHelp()
      return Map.empty
    }

    // Parse explicit key-value arguments
    val params = params_raw.filter(_.contains("="))
    val settings_keyVal = ParseKeyValuePairs(params)

    // Grab any implicitly assigned arguments
    if (settings_keyVal.contains("project")) {
      settings_keyVal
    } else {
      settings_keyVal + ("project" -> params_raw(0))
    }
  }

  def writeToDisk(code: Seq[GeneratedSourceFile], settings: Map[String, String]) {
    FileUtils.writeIfChanged(code, settings.get("output_path"))
  }

}