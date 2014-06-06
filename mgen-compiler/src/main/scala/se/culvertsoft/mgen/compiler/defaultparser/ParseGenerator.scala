package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsJavaMap

import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.plugins.GeneratorDescriptor

object ParseGenerator {
  def apply(node: scala.xml.Node): GeneratorDescriptor = {
    val name = node.getAttribString("name").getOrElse(ThrowRTE("Generator missing attribute name"))
    val classPath = node.getNodeContents("generator_class_path").toString
    val settings = node.getStringStringMap()
    new GeneratorDescriptor(name, classPath, settings)
  }
}