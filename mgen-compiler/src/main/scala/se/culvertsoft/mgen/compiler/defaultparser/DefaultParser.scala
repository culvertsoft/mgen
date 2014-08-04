package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.impl.ProjectImpl
import se.culvertsoft.mgen.api.plugins.Parser
import se.culvertsoft.mgen.compiler.components.CheckConflicts
import se.culvertsoft.mgen.compiler.components.LinkTypes
import se.culvertsoft.mgen.compiler.util.FileUtils
import se.culvertsoft.mgen.compiler.util.ThrowRTE

class DefaultParser(allowNoGenerators: Boolean) extends Parser {
  def this() = this(false) // This explicit aux constructor is required for java reflection to work

  implicit val parsed = new HashMap[String, ProjectImpl]

  override def parse(settings: java.util.Map[String, String]): Project = {

    val projectPath: String = settings.get("project")
    if (projectPath == null) ThrowRTE("Missing '-project' cmd line argument")

    FileUtils.checkiSsFileOrThrow(projectPath)

    ParseProject(projectPath, settings.toMap, Nil, null)

  }

}