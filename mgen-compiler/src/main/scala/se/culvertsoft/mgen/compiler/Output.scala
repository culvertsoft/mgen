package se.culvertsoft.mgen.compiler

import java.io.File
import java.util.ArrayList
import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.bufferAsJavaList
import scala.reflect.io.Path
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.GeneratedSourceFile
import se.culvertsoft.mgen.api.plugins.Generator
import se.culvertsoft.mgen.compiler.defaultparser.FileUtils
import java.nio.file.Files
import java.nio.file.Paths
import se.culvertsoft.mgen.compiler.defaultparser.ThrowRTE
import java.nio.charset.Charset

object Output {

  val charset = Charset.forName("UTF8");

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

  def write(outputs: Seq[GeneratedSourceFile], outputPathPrepend: Option[String] = None) {

    println("Writing files to disk:")

    for (output <- outputs) {

      val filePath = outputPathPrepend match {
        case Some(prepend) => prepend + File.separator + output.filePath
        case _ => output.filePath
      }

      val file = new File(filePath)
      if (!file.exists || FileUtils.readToString(filePath, charset) != output.sourceCode) {
        println(s"  writing: ${filePath}")
        if (!file.exists) {
          val dir = FileUtils.directoryOf(filePath)
          Path(dir).createDirectory(true, false)
        }
        Files.write(Paths.get(filePath), output.sourceCode.getBytes(charset));
      } else {
        println(s"  skipping (no change): ${filePath}")
      }

    }

    println("")

  }

}