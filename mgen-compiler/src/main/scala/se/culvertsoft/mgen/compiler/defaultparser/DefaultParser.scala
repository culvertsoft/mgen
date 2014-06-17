package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsScalaMap

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

}