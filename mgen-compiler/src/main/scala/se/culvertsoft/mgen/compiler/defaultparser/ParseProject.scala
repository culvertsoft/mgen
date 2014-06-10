package se.culvertsoft.mgen.compiler.defaultparser

import java.io.File

import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.xml.XML.loadFile

import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.impl.ProjectImpl

object ParseProject {

  private def apply(
    filePath: String,
    project: ProjectImpl,
    rootProject: ProjectImpl,
    settings0: Map[String, String] = Map[String, String]())(implicit cache: ParseState): ProjectImpl = {

    cache.fileParsing.projects.getOrElseUpdate(filePath, {

      println(s"parsing project: ${project.filePath} (${filePath})")

      val dir = FileUtils.directoryOf(filePath) + File.separatorChar

      // Read in project xml source code 
      val projectXml = scala.xml.Utility.trim(loadFile(filePath))
      if (projectXml.label.toLowerCase() != "project") {
        throw new RuntimeException(s"Tried to load $filePath as project, but it was not a project file!")
      }

      // Parse settings
      val settings = settings0 ++ projectXml.getSettings()
      project.setSettings(settings)

      // Parse dependencies
      val rootDir = FileUtils.directoryOf(rootProject.filePath) + File.separatorChar
      val dependFilePaths = projectXml.getAllNodeContents("Depend").map { rootDir + _ }
      val dependencies = dependFilePaths.map { ParseProject(_, rootProject, settings) }
      project.setDependencies(dependencies)

      // Parse modules
      val moduleFilePaths = projectXml.getAllNodeContents("Module").map { dir + _ }
      val modules = moduleFilePaths.map { ParseModule(_, settings) }
      project.setModules(modules)

      // Parse Generators
      val generators = (projectXml \ "Generator") map ParseGenerator.apply
      project.setGenerators(generators)

      project

    })

  }

  def apply(
    filePath: String,
    rootProject: ProjectImpl,
    settings: Map[String, String])(implicit cache: ParseState): ProjectImpl = {

    FileUtils.checkiSsFileOrThrow(filePath)

    val path = FileUtils.removeFileEnding(FileUtils.nameOf(filePath))
    val project = new ProjectImpl(path, filePath, false)

    ParseProject(filePath, project, rootProject, settings)

  }

  def apply(
    filePath: String,
    settings: Map[String, String])(implicit cache: ParseState): ProjectImpl = {

    FileUtils.checkiSsFileOrThrow(filePath)

    val path = FileUtils.removeFileEnding(FileUtils.nameOf(filePath))
    val project = new ProjectImpl(path, filePath, true)

    ParseProject(filePath, project, project, settings)

  }

}

