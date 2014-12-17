package se.culvertsoft.mgen.compiler.components

import java.util.ArrayList

import scala.Option.option2Iterable
import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.exceptions.AnalysisException
import se.culvertsoft.mgen.api.exceptions.GenerationException
import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Generator

object GenerateCode {

  def apply(settings: Map[String, String]): Seq[GeneratedSourceFile] = {
    val pluginFinder = new PluginFinder(settings.getOrElse("plugin_paths", ""), settings.getOrElse("use_env_vars", "true").toBoolean)
    val project = CreateProject(settings, pluginFinder)
    RemoveParkedFields(project)
    GenerateCode(project, settings, pluginFinder)
  }
  
  def apply(
    project: Project,
    generators: Seq[Generator]): Seq[GeneratedSourceFile] = {

    print("Generating code...")

    val out = new ArrayList[GeneratedSourceFile]

    for (genSpec <- project.generators()) {
      val optGenerator = generators.find(_.getClass.getName == genSpec.getGeneratorClassPath)

      if (optGenerator.isDefined) {
        val generator = optGenerator.get
        val generatorSettings = genSpec.getGeneratorSettings()
        val generated = generator.generate(project, generatorSettings)
        out.addAll(generated)
      }

    }

    println("ok\n")

    out

  }

  def apply(project: Project, settings: Map[String, String], pluginFinder: PluginFinder): Seq[GeneratedSourceFile] = {

    val failOnMissingGenerator = settings.getOrElse("fail_on_missing_generator", "false").toBoolean

    // Find our selected generators
    val selectedGenerators = project.generators()
    if (selectedGenerators.isEmpty)
      throw new AnalysisException(s"No generator specified, check your project file")

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

    generatedSources

  }

}