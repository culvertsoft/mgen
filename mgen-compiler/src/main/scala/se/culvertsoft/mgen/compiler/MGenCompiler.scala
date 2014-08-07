package se.culvertsoft.mgen.compiler

import scala.Array.canBuildFrom
import scala.util.Failure
import scala.util.Success
import scala.util.Try

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
    PrintIntro("0.x")

    val compileResult = Try {

      // Parse input parameters
      val params = paramsUntrimmed.map(_.trim()).toBuffer
      if (params.contains("-help")) {
        PrintHelp()
        return
      }

      // Parse cmd line args      
      val settings = ParseKeyValuePairs(params)

      // Load plugins
      val pluginFinder = new PluginFinder(settings.getOrElse("plugin_paths", ""))

      // Parse sources and link together types, fields, super types and default values
      val project = CreateProject(settings, pluginFinder)

      // Generate code
      val code = GenerateCode(project, settings, pluginFinder)

      // Output the code to disk
      FileUtils.writeIfChanged(code, settings.get("output_path"))

    }

    compileResult match {
      case Success(_) =>
        println("*** COMPILATION SUCCESS ***")
      case Failure(err) =>
        println
        println("*** COMPILATION FAILED (see error log below) ***")
        println
        PrintHelp
        println
        System.err.flush()
        System.out.flush()
        throw err
    }

  }

}