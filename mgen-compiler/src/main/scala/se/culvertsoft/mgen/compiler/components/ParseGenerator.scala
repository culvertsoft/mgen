package se.culvertsoft.mgen.compiler.components

import scala.collection.JavaConversions.mapAsJavaMap

import se.culvertsoft.mgen.api.model.impl.GeneratorDescriptorImpl
import se.culvertsoft.mgen.compiler.util.ThrowRTE
import se.culvertsoft.mgen.compiler.util.XmlUtils.RichXmlNode

object ParseGenerator {
  def apply(node: scala.xml.Node): GeneratorDescriptorImpl = {
    val name = node.getAttribString("name").getOrElse(ThrowRTE("Generator missing attribute name"))
    val classPath = node.getNodeContents("class_path").toString
    val settings = node.getStringStringMap()
    new GeneratorDescriptorImpl(name, classPath, settings)
  }
}