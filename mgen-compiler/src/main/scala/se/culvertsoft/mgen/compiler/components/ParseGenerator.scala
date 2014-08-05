package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.mapAsJavaMap

import se.culvertsoft.mgen.api.model.GeneratorDescriptor
import se.culvertsoft.mgen.compiler.util.XmlUtils.RichXmlNode

object ParseGenerator {
  def apply(node: scala.xml.Node): GeneratorDescriptor = {
    val name = node.getAttribString("name").getOrElse(throw new RuntimeException("Generator missing attribute name"))
    val classPath = node.getNodeContents("class_path").toString
    val settings = node.getStringStringMap()
    new GeneratorDescriptor(name, classPath, settings)
  }
}