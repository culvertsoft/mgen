package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.seqAsJavaList
import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.util.CRC16

object ParseField {

  def apply(
    node: scala.xml.Node,
    ownerClassName: String)(implicit cache: ParseState): Field = {

    val name = node.label
    val typeString = node.getAttribString("type").getOrElse(ThrowRTE(s"Missing type attribute for field ${name}"))
    val typ = ParseFieldType(typeString)
    val flagString = node.getAttribString("flags").getOrElse("")
    val idOverride = node.getAttribString("id").map(java.lang.Short.decode(_).shortValue()).getOrElse(CRC16.calc(name))
    new Field(
      ownerClassName,
      name,
      typ,
      if (flagString.size > 0) flagString.split(',').map(_.trim).toSeq else Seq.empty[String],
      idOverride)

  }

}