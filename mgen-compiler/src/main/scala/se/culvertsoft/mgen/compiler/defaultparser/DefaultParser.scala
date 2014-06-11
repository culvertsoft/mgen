package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Parser

class DefaultParser(allowNoGenerators: Boolean) extends Parser {
  def this() = this(false) // This explicit aux constructor is required for java reflection to work

  implicit private val cache = new ParseState

  override def parse(settings: java.util.Map[String, String]): Project = {

    val projectPath: String = settings.get("project")
    if (projectPath == null) ThrowRTE("Missing '-project' cmd line argument")

    FileUtils.checkiSsFileOrThrow(projectPath)

    val project = ParseProject(projectPath, settings.toMap, Nil, true)

    LinkTypes(project)

    project

  }

  private def print(p: Project) {
    println(s"PROJECT ${p.name()}:")
    println("deps:")
    for (d <- p.dependencies())
      print(d)
    for (m <- p.modules())
      print(m)
  }

  private def print(m: Module) {
    println(s"MODULE: ${m.path()}")
    for ((a, t) <- m.types())
      print(t)
  }

  private def print(t: CustomType) {

    println(s"TYPE: ${t.fullName} : ${t.superType()}")
    for (f <- t.fields()) {
      println("  - " + f)
    }
  }

}