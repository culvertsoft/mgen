package se.culvertsoft.mgen.idlparser.util

import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.PrettyPrinter

object XmlOps {

  def mkPrinter() = new PrettyPrinter(120, 4)

  implicit class PrettyXmlNode(nodes: Node) {
    def mkPretty(): String = mkPrinter().format(nodes).toString()
  }

  implicit class PrettyXmlNodes(nodes: Seq[Node]) {
    def mkPretty(): String = mkPrinter().formatNodes(nodes).toString()
  }

  implicit class PrettyXmlNodes2(nodes: NodeSeq) {
    def mkPretty(): String = mkPrinter().formatNodes(nodes).toString()
  }

  implicit class TrimNodeOps(node: Node) {
    def trim(): Node = scala.xml.Utility.trim(node)
  }

  implicit class TrimNodesOps(nodes: NodeSeq) {
    def trim(): NodeSeq = nodes.map(_.trim)
  }

}