package se.culvertsoft.mgen.compiler

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.compiler.components.CreateProject
import se.culvertsoft.mgen.compiler.components.GenerateCode
import se.culvertsoft.mgen.compiler.components.ParseCmdLineArgs
import se.culvertsoft.mgen.compiler.components.PluginLoader
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

      val pluginLoader = createPluginLoader(settings)

      val project = createProject(settings, pluginLoader)

      val code = generateCode(settings, project, pluginLoader)

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

  def parseCmdLineSettings(cmdLineArgs: Array[String]): Map[String, String] = {
    ParseCmdLineArgs(cmdLineArgs)
  }

  def createPluginLoader(settings: Map[String, String]): PluginLoader = {
    new PluginLoader(settings.getOrElse("plugin_paths", ""), settings.getOrElse("use_env_vars", "true").toBoolean)
  }

  def createProject(settings: Map[String, String], pluginLoader: PluginLoader): Project = {
    CreateProject(settings, pluginLoader)
  }

  def generateCode(settings: Map[String, String], project: Project, pluginLoader: PluginLoader): Seq[GeneratedSourceFile] = {
    GenerateCode(settings, project, pluginLoader)
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