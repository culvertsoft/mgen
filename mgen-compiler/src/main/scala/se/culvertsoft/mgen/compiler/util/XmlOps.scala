package se.culvertsoft.mgen.compiler.util

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
  /**
   *
   * def textElem(name: String, text: String) = Elem(null, name, Null, TopScope, Text(text))
   *
   * /*
   * var prev: scala.xml.MetaData = None
   * val attribs = mkAttribsMap() map { pair =>
   * prev = Attribute(null, pair._1, pair._2, prev)
   * prev
   * }
   * val meta = attribs.fold(Null)((soFar, attr) => soFar append attr)*/    /*
   * val elem = new scala.xml.Elem(
   * null,
   * typ.shortName,
   * null,
   * scala.xml.TopScope,
   * false,
   * types)
   * */
   *
   */
}