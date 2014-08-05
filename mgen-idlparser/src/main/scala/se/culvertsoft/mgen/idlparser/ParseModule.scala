package se.culvertsoft.mgen.idlparser

import java.io.File

import scala.collection.JavaConversions.mapAsJavaMap
import scala.collection.JavaConversions.seqAsJavaList
import scala.xml.XML.loadFile

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.idlparser.util.XmlUtils.RichXmlNode

object ParseModule {

  def apply(
    file: File,
    settings0: Map[String, String],
    project: Project) {

    val absoluteFilePath = file.getCanonicalPath()

    println(s"  parsing module: ${absoluteFilePath}")

    // Calculate module path
    val modulePath = file.getName.split('.').dropRight(1).mkString(".")

    // Read in module xml source code 
    val moduleXml = scala.xml.Utility.trim(loadFile(file))
    if (moduleXml.label.toLowerCase() != "module") {
      throw new RuntimeException(s"Tried to load ${file.getPath} as module, but it was not a module file!")
    }

    // Parse settings
    val settings = settings0 ++ moduleXml.getSettings()

    // Create the module
    val module = project.getOrCreateModule(modulePath, file.getPath, absoluteFilePath, settings);

    // Parse enumerations
    val enumsXml = moduleXml.getAllNodeContents("Enums")
    val enums = enumsXml.map { ParseEnum(_, module) }
    module.addEnums(enums)

    // Parse types
    val typesXml = moduleXml.getAllNodeContents("Types")
    val types = typesXml.map { ParseType(_, module) }
    module.addClasses(types)

  }

}