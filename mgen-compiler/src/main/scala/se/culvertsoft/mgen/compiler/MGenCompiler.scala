package se.culvertsoft.mgen.compiler

import scala.Array.canBuildFrom
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.compiler.components.CreateProject
import se.culvertsoft.mgen.compiler.components.GenerateCode
import se.culvertsoft.mgen.compiler.components.PluginFinder
import se.culvertsoft.mgen.compiler.internal.PrintHelp
import se.culvertsoft.mgen.compiler.internal.PrintIntro
import se.culvertsoft.mgen.compiler.util.FileUtils
import se.culvertsoft.mgen.compiler.util.ParseKeyValuePairs

object MGenCompiler {

  def main(paramsUntrimmed: Array[String]) {

    // Print introduction
    PrintIntro(s"(${BuildVersion.GIT_TAG} ${BuildVersion.GIT_COMMIT_DATE})")

    val compileResult = Try {

      // Parse input parameters
      val params_raw = paramsUntrimmed.map(_.trim()).toBuffer
      if (params_raw.isEmpty || params_raw.contains("-help")) {
        PrintHelp()
        return
      }

      // Parse explicit key-value arguments
      val params = params_raw.filter(_.contains("="))
      val settings_keyVal = ParseKeyValuePairs(params)

      // Grab any implicitly assigned arguments
      val settings =
        if (settings_keyVal.contains("project")) {
          settings_keyVal
        } else {
          settings_keyVal + ("project" -> params_raw(0))
        }

      // Parse input, generate code, ... the work
      val code = run(settings)

      // Output the code to disk
      FileUtils.writeIfChanged(code, settings.get("output_path"))

    }

    compileResult match {
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

  def run(settings: Map[String, String]): Seq[GeneratedSourceFile] = {
    val pluginFinder = new PluginFinder(settings.getOrElse("plugin_paths", ""))
    val project = CreateProject(settings, pluginFinder)
    GenerateCode(project, settings, pluginFinder)
  }

}