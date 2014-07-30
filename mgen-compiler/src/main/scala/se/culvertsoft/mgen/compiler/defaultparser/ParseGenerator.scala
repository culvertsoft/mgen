package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.mapAsJavaMap

import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.impl.GeneratorDescriptorImpl

object ParseGenerator {
  def apply(node: scala.xml.Node): GeneratorDescriptorImpl = {
    val name = node.getAttribString("name").getOrElse(ThrowRTE("Generator missing attribute name"))
    val classPath = node.getNodeContents("generator_class_path").toString
    val settings = node.getStringStringMap()
    new GeneratorDescriptorImpl(name, classPath, settings)
  }
}