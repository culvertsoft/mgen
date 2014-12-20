package se.culvertsoft.mgen.compiler.components

import scala.Array.canBuildFrom
import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.HashMap
import scala.xml.XML.loadFile

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Parser
import se.culvertsoft.mgen.compiler.util.EnvVarUtils
import se.culvertsoft.mgen.compiler.util.FileUtils
import se.culvertsoft.mgen.compiler.util.XmlUtils.RichXmlNode
import se.culvertsoft.mgen.idlparser.IdlParser

object ParseProject {

  def apply(
    settings: Map[String, String],
    pluginFinder: PluginLoader): Project = {

    val projectPath =
      settings
        .get("project")
        .getOrElse(throw new RuntimeException("Missing '-project' cmd line argument"))

    val paramPaths = settings.get("include_paths").getOrElse("").split(",")

    val envPaths =
      if (settings.getOrElse("use_env_vars", "true").toBoolean)
        EnvVarUtils.getCommaSeparated("MGEN_INCLUDE_PATHS")
      else
        Array[String]()

    val includePaths = (paramPaths ++ envPaths).distinct

    FileUtils.checkiSsFileOrThrow(projectPath)

    ParseProject(projectPath, settings, includePaths, null, new HashMap[String, Project], pluginFinder)

  }

  def apply(
    filePath: String,
    settings0: Map[String, String],
    searchPaths0: Seq[String],
    parent: Project,
    alreadyParsed: HashMap[String, Project],
    pluginFinder: PluginLoader): Project = {

    val file = FileUtils.findFile(filePath, searchPaths0)
      .getOrElse(throw new RuntimeException(s"Could not find referenced project file: ${filePath}"))

    val absoluteFilePath = file.getCanonicalPath()

    alreadyParsed.get(absoluteFilePath) match {
      case Some(p) => p
      case _ =>
        println(s"  parsing project: ${absoluteFilePath}")

        val projectName = FileUtils.removeFileEnding(FileUtils.nameOf(absoluteFilePath))
        val projectDir = FileUtils.directoryOf(absoluteFilePath)
        val project = new Project(projectName, filePath, file.getAbsolutePath(), parent)
        alreadyParsed.put(absoluteFilePath, project)

        val searchPaths: Seq[String] = searchPaths0 ++ Seq(projectDir)

        // Read in project xml source code 
        val projectXml = scala.xml.Utility.trim(loadFile(file))
        if (projectXml.label.toLowerCase() != "project") {
          throw new RuntimeException(s"Tried to load $filePath as project, but it was not a project file!")
        }

        // Parse settings
        val settings = settings0 ++ projectXml.getSettings()
        project.setSettings(settings)

        // Parse Generators
        val generators = (projectXml \ "Generator") map ParseGenerator.apply
        project.setGenerators(generators)

        // Parse dependencies
        val dependFilePaths = projectXml.getAllNodeContents("Dependency").map(_.toString)
        val dependencies = dependFilePaths.map(ParseProject(_, settings, searchPaths, project, alreadyParsed, pluginFinder))
        project.setDependencies(dependencies)

        // Parse sources
        val sourcesNodes = (projectXml \ "Sources")
        sourcesNodes foreach { sourcesNode =>
          val sourcesSettings = settings ++ sourcesNode.getStringStringMap()
          val parserName = sourcesNode.getAttribString("parser").getOrElse(classOf[IdlParser].getName())
          val parser = pluginFinder.getThreadLocal[Parser](parserName).getOrElse(throw new RuntimeException(s"Parser not found: Unknown parser $parserName in project file $absoluteFilePath"))
          val sourceFileNames = (sourcesNode \ "Source") map (_.text)
          val files = sourceFileNames.map { fileName =>
            FileUtils.findFile(fileName, searchPaths)
              .getOrElse(throw new RuntimeException(s"Could not find source file: ${fileName} specified for parser ${parserName} in project ${absoluteFilePath}"))
          }

          parser.parse(files, sourcesSettings, project)
        }

        project

    }

  }
}