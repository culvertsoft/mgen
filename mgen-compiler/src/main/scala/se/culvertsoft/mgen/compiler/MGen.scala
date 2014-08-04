package se.culvertsoft.mgen.compiler

import scala.Array.canBuildFrom
import scala.Option.option2Iterable
import scala.collection.JavaConversions.asScalaBuffer
import scala.util.Failure
import scala.util.Success
import scala.util.Try

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.compiler.components.CheckConflicts
import se.culvertsoft.mgen.compiler.components.GenerateCode
import se.culvertsoft.mgen.compiler.components.LinkTypes
import se.culvertsoft.mgen.compiler.components.ParseProject
import se.culvertsoft.mgen.compiler.components.PluginFinder
import se.culvertsoft.mgen.compiler.util.FileUtils

object MGen {

  val VERSION = "0.x"

  def main(paramsUntrimmed: Array[String]) {

    // Print introduction
    printIntro()

    val compileResult = Try {

      // Parse input parameters
      val params = paramsUntrimmed.map(_.trim()).toBuffer
      if (params.contains("-help")) {
        printHelp()
        return
      }

      // Parse cmd line args      
      val settings = parseKeyValuePairs(params)
      val failOnMissingGenerator = settings.getOrElse("fail_on_missing_generator", "false").toBoolean
      val checkForConflicts = settings.get("check_conflicts").map(_.toBoolean).getOrElse(true)

      // Load plugins
      val pluginPaths = split(settings.getOrElse("plugin_paths", ""))
      val pluginFinder = new PluginFinder(pluginPaths)

      // Parse the project shallow
      println("Parsing project...")
      val project = ParseProject(settings, pluginFinder)
      println("ok\n")

      // Link custom types and enums
      println("Linking types...")
      LinkTypes(project)
      println("ok\n")

      // Check for type conflicts (ids, names, hashes etc)
      if (checkForConflicts) {
        println("Checking for type conflicts...")
        CheckConflicts(project)
        println("ok\n")
      }

      // Find our selected generators
      val selectedGenerators = project.generators()
      if (selectedGenerators.isEmpty)
        throw new AnalysisException(s"No generator specified, check your project file")

      // Instantiate generators
      println("Instantiating generators...")
      val generators = selectedGenerators.flatMap { selected =>
        val clsName = selected.getGeneratorClassPath
        pluginFinder.find[Generator](clsName) match {
          case Some(cls) =>
            println(s"Created generator: ${clsName}")
            Some(cls.newInstance())
          case None =>
            if (failOnMissingGenerator)
              throw new GenerationException(s"Could not find specified generator '${clsName}'")
            println(s"WARNING: Could not find specified generator '${clsName}', skipping")
            None
        }
      }
      println()

      // Verify we were able to create some generators
      if (generators.isEmpty)
        throw new AnalysisException(s"Failed to instantiate any of the specified generators (${selectedGenerators})")

      // Run the generators
      val generatedSources = GenerateCode(project, generators)

      // Check that we actually generated some code
      if (generatedSources.isEmpty)
        throw new AnalysisException(s"Generators generated no code...")

      // Write the actual code to disk 
      FileUtils.writeIfChanged(generatedSources, settings.get("output_path"))

    }

    compileResult match {
      case Success(_) =>
        println("*** COMPILATION SUCCESS ***")
      case Failure(err) =>
        println
        println("*** COMPILATION FAILED (see error log below) ***")
        println
        printHelp
        println
        System.err.flush()
        System.out.flush()
        throw err
    }

  }

  def split(s: String): Seq[String] = {
    s.split(",").map(_.trim()).toSeq
  }

  def printIntro() {
    println(s"                                           ")
    println(s"    ***************************************")
    println(s"    **                                   **")
    println(s"    **                                   **")
    println(s"    **        MGen Compiler v$VERSION         **")
    println(s"    **                                   **")
    println(s"    ***************************************")
    println(s"                                           ")
  }

  def printHelp() {
    println(s"Valid MGen compiler arguments are: ")
    println("  -help: displays this help ")
    println("  -project=\"myProjectFile.xml\": specify project file (Required)")
    println("  -plugin_paths=\"my/external/path1, my/external/path2\": specify additional plugin paths (Optional) ")
    println("  -output_path=\"specify output path (Optional) ")
    println("  -fail_on_missing_generator=true/false: Default false (Optional)")
    println("  -check_conflicts=\"true/false\" (default=true): If false: the compiler will ignore any type name/id/hash conflicts (Optional). Useful for IDL<->IDL translation")
  }

  def trimKeyVal(in: String): String = {

    if (in == null)
      return ""

    val out = in.trim
    if (out.startsWith("\"") && out.endsWith("\""))
      out.substring(1, out.length() - 1)
    else
      out
  }

  def parseKeyValuePairs(params: Seq[String]): Map[String, String] = {

    print("Parsing command line args...")
    try {
      val settings = params.map(_.split("="))
        .map(arr => (
          trimKeyVal(arr(0).filter(_ != '-').toLowerCase()),
          trimKeyVal(arr(1))))
        .toMap
      println("ok")
      for ((key, value) <- settings) {
        println(s"  $key: $value")
      }
      println("")
      settings
    } catch {
      case t: Exception =>
        throw new RuntimeException("Failed parsing key-value pairs from command line arguments", t)
    }
  }

}