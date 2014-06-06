package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.seqAsJavaList

import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.Field

object ParseField {

  def apply(
    node: scala.xml.Node,
    ownerClassName: String)(implicit cache: ParseState): Field = {

    val name = node.label
    val typeString = node.getAttribString("type").getOrElse(ThrowRTE(s"Missing type attribute for field ${name}"))
    val typ = ParseFieldType(typeString)
    val flags = node.getAttribString("flags")
      .getOrElse("")
      .split(',')
      .map(_.trim)

    new Field(ownerClassName, name, typ, flags.toSeq)

  }

}