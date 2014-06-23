package se.culvertsoft.mgen.compiler

import java.util.ArrayList
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.reflect.io.Path
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.api.plugins.Generator
import java.io.File
import se.culvertsoft.mgen.compiler.defaultparser.FileUtils

object Output {

  def assemble(
    project: Project,
    generators: Map[String, Generator]): Seq[GeneratedSourceFile] = {

    print("Generating code...")

    val out = new ArrayList[GeneratedSourceFile]

    val dependentModules = project.dependencies.flatMap(_.modules())
    val rootModules = project.modules

    val allModules = rootModules ++ dependentModules

    for (genSpec <- project.generators()) {
      val optGenerator = generators.get(genSpec.getGeneratorClassPath())

      if (optGenerator.isDefined) {

        val generator = optGenerator.get
        val generatorSettings = genSpec.getGeneratorSettings()
        val generated = generator.generate(allModules, generatorSettings)
        out.addAll(generated)

      }

    }

    println("ok\n")

    out

  }

  def write(outputs: Seq[GeneratedSourceFile]) {

    println("Writing files to disk:")

    for (output <- outputs) {

      val file = new File(output.filePath)
      if (!file.exists || FileUtils.readToString(output.filePath) != output.sourceCode) {
        println(s"  writing: ${output.filePath}")
        val dir = FileUtils.directoryOf(output.filePath)
        Path(dir).createDirectory(true, false)
        Path(output.filePath()).toFile.writeAll(output.sourceCode())
      } else {        
        println(s"  skipping (no change): ${output.filePath}")
      }

    }

    println("")

  }

}