package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.xml.XML.loadFile

import se.culvertsoft.mgen.api.model.impl.ProjectImpl
import se.culvertsoft.mgen.compiler.util.FileUtils
import se.culvertsoft.mgen.compiler.util.ThrowRTE
import se.culvertsoft.mgen.compiler.util.XmlUtils.RichXmlNode

object ParseProject {

  def apply(
    filePath: String,
    settings0: Map[String, String],
    searchPaths0: Seq[String],
    parent: ProjectImpl)(implicit cache: ParseState): ProjectImpl = {

    val file = FileUtils.findFile(filePath, searchPaths0)
      .getOrElse(ThrowRTE(s"Could not find referenced project file: ${filePath}"))

    val absoluteFilePath = file.getCanonicalPath()

    cache.fileParsing.projects.getOrElseUpdate(absoluteFilePath, {

      println(s"parsing project: ${absoluteFilePath}")

      val projectName = FileUtils.removeFileEnding(FileUtils.nameOf(absoluteFilePath))
      val projectDir = FileUtils.directoryOf(absoluteFilePath)
      val project = new ProjectImpl(projectName, filePath, file.getAbsolutePath(), parent)

      val searchPaths: Seq[String] = searchPaths0 ++ Seq(projectDir)

      // Read in project xml source code 
      val projectXml = scala.xml.Utility.trim(loadFile(file))
      if (projectXml.label.toLowerCase() != "project") {
        throw new RuntimeException(s"Tried to load $filePath as project, but it was not a project file!")
      }

      // Parse settings
      val settings = settings0 ++ projectXml.getSettings()
      project.setSettings(settings)

      // Parse dependenciesw
      val dependFilePaths = projectXml.getAllNodeContents("Depend").map(_.toString)
      val dependencies = dependFilePaths.map(ParseProject(_, settings, searchPaths, project))
      project.setDependencies(dependencies)

      // Parse modules
      val moduleFilePaths = projectXml.getAllNodeContents("Module").map { _.toString }
      val modules = moduleFilePaths.map { ParseModule(_, settings, searchPaths, project) }
      project.setModules(modules)

      // Parse Generators
      val generators = (projectXml \ "Generator") map ParseGenerator.apply
      project.setGenerators(generators)

      project

    })

  }

}

