package se.culvertsoft.mgen.compiler.util

import java.util.ArrayList

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.GeneratedSourceFile
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Generator

object GenerateCode {

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

}