package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.xml.XML.loadFile

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.impl.ModuleImpl
import se.culvertsoft.mgen.compiler.util.FileUtils
import se.culvertsoft.mgen.compiler.util.ThrowRTE
import se.culvertsoft.mgen.compiler.util.XmlUtils.RichXmlNode

object ParseModule {

  def apply(
    filePath: String,
    settings0: Map[String, String],
    searchPaths0: Seq[String],
    parent: Project): ModuleImpl = {

    val file = FileUtils.findFile(filePath, searchPaths0)
      .getOrElse(ThrowRTE(s"Could not find referenced module file: ${filePath}"))

    val absoluteFilePath = file.getCanonicalPath()

    println(s"parsing module: ${absoluteFilePath}")

    // Calculate module path
    val modulePath = FileUtils.removeFileEnding(FileUtils.nameOf(filePath))

    // Read in module xml source code 
    val moduleXml = scala.xml.Utility.trim(loadFile(file))
    if (moduleXml.label.toLowerCase() != "module") {
      throw new RuntimeException(s"Tried to load $filePath as module, but it was not a module file!")
    }

    // Parse settings
    val settings = settings0 ++ moduleXml.getSettings()

    // Create the module
    val module = new ModuleImpl(
      modulePath,
      filePath,
      absoluteFilePath,
      settings,
      parent)

    // Parse enumerations
    val enumsXml = moduleXml.getAllNodeContents("Enums")
    val enums = enumsXml.map { ParseEnum(_, module) }
    module.setEnums(enums)

    // Parse types
    val typesXml = moduleXml.getAllNodeContents("Types")
    val types = typesXml.map { ParseType(_, module) }
    module.setTypes(types)

    module

  }

}