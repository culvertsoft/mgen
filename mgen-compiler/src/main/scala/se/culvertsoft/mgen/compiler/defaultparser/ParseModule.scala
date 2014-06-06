package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.xml.XML.loadFile

import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.impl.ModuleImpl

object ParseModule {

  def apply(
    filePath: String,
    settings0: Map[String, String])(implicit cache: ParseState): ModuleImpl = {

    cache.fileParsing.modules.getOrElseUpdate(filePath, {

      FileUtils.checkiSsFileOrThrow(filePath)

      // Calculate module path
      val path = FileUtils.removeFileEnding(FileUtils.nameOf(filePath))

      println(s"parsing module: ${path} (${filePath})")

      // Read in module xml source code 
      val moduleXml = scala.xml.Utility.trim(loadFile(filePath))
      if (moduleXml.label.toLowerCase() != "module") {
        throw new RuntimeException(s"Tried to load $filePath as module, but it was not a module file!")
      }

      // Parse settings
      val settings = settings0 ++ moduleXml.getSettings()

      // Create the module
      val module = new ModuleImpl(path, settings)

      // Parse types
      val typesXml = moduleXml.getAllNodeContents("Types")
      val types = typesXml.map { ParseType(_, module) }
      module.setTypes(types)

      module

    })

  }

}