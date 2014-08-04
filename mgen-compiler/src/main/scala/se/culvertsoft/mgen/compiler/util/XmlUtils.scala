package se.culvertsoft.mgen.compiler.util

object XmlUtils {

  implicit class RichXmlNode(node: scala.xml.Node) {

    def getAllNodeContents(named: String): Seq[scala.xml.Node] = {
      (node \ named).flatMap(_.child)
    }

    def getNodeContents(named: String): scala.xml.Node = {
      getAllNodeContents(named).headOption.getOrElse(throw new RuntimeException(s"Missing node <${named}>"))
    }

    def getOptNodeContents(named: String): Option[scala.xml.Node] = {
      getAllNodeContents(named).headOption
    }

    def getStringStringMap(): Map[String, String] = {
      node.child.map(node => (node.label, node.text)).toMap
    }

    def getSettings(): Map[String, String] = {
      getOptNodeContents("Settings").map(_.getStringStringMap).getOrElse(Map[String, String]())
    }

    def getAttribString(attribName: String): Option[String] = {
      node.attribute(attribName).map(_.head.text)
    }

  }

}