package se.culvertsoft.mgen.compiler

import scala.Array.canBuildFrom
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.compiler.plugins.FindPlugins

object MGen {

  val DEFAULT_PARSER = "se.culvertsoft.mgen.compiler.defaultparser.DefaultParser"

  val VERSION = "0.x"

  def main(paramsUntrimmed: Array[String]) {

    // Print introduction
    printIntro()

    // Parse input parameters
    val params = paramsUntrimmed.map(_.trim()).toBuffer
    if (params.contains("-help")) {
      printHelp()
      return
    }

    // Parse cmd line args      
    val settings = parseKeyValuePairs(params)

    // Detect available plugins
    println("Detecting available plugins")
    val findPlugins = new FindPlugins(split(settings.getOrElse("plugin_paths", "")))
    val availableParsers = findPlugins.parserClasses
    val availableGenerators = findPlugins.generatorClasses
    println(s"  --> detected available parsers: ${availableParsers.keys.mkString(", ")}")
    println(s"  --> detected available generators: ${availableGenerators.keys.mkString(", ")}")
    println("")
    if (availableParsers.isEmpty)
      throw new AnalysisException(s"Could not find any parsers")
    if (availableGenerators.isEmpty)
      throw new AnalysisException(s"Could not find any generators")

    // Ensure parsers are specified
    val parserPath =
      settings.get("parser") match {
        case Some(parserPath) =>
          println(s"INFO: Using parser '$parserPath'")
          println("")
          parserPath
        case _ =>
          println(s"INFO: Using default parser '$DEFAULT_PARSER' (No -parser specified)")
          println("")
          DEFAULT_PARSER
      }

    // Instantiate the parser
    print("Instantiating parser...")
    val parser =
      availableParsers.get(parserPath) match {
        case Some(parserClass) => parserClass.newInstance()
        case _ => throw new AnalysisException(s"Aborting: Specified parser class '${parserPath}' not found")
      }
    println("ok\n")

    // Run the parsers
    println("Executing parser...")
    val project = parser.parse(settings)
    println("ok\n")

    // Find our selected generators
    val selectedGenerators = project.generators()
    if (selectedGenerators.isEmpty)
      throw new AnalysisException(s"No generator specified, check your project file")

    // Instantiate the generators
    val generators = new HashMap[String, Generator]
    selectedGenerators.foreach({ selected =>
      val clsName = selected.getGeneratorClassPath()
      availableGenerators.get(clsName) match {
        case Some(cls) =>
          generators.put(clsName, cls.newInstance())
          println(s"Created generator: ${clsName}")
        case None =>
          println(s"WARNING: Could not find specified generator '${clsName}', skipping")
      }
    })
    println()

    // Verify we were able to create some generators
    if (generators.isEmpty)
      throw new AnalysisException(s"Failed to instantiate any of the specified generators (${selectedGenerators})")

    // Run the generators
    val generatedSources = Output.assemble(project, generators.toMap)

    // Check that we actually generated some code
    if (generatedSources.isEmpty)
      throw new AnalysisException(s"Generators generated no code...")

    // Write the actual code to disk 
    // TODO: don't overwrite existing identical files (build times)
    Output.write(generatedSources, settings.get("output_path"))

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
    println(s"Valid arguments are: ")
    println("  -help: displays this help ")
    println("  -project=\"myProjectFile.xml\": specify project file (Required)")
    println("  -parser=\"se.culvertsoft.Dummyparser,se.coocoo.MyParser\": specify IDL parser (Optional) ")
    println("  -linker=\"se.culvertsoft.dummyLinker.Linker1\": specify module linker parser (Optional) ")
    println("  -plugin_paths=\"my/external/path1, my/external/path2\": specify additional plugin paths (Optional) ")
    println("  -output_path=\"specify output path (Optional) ")
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