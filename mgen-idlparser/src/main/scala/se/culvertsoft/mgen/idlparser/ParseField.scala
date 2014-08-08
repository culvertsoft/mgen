package se.culvertsoft.mgen.idlparser

import scala.Array.canBuildFrom
import scala.collection.JavaConversions.seqAsJavaList
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.model.Field
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.idlparser.util.XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.ClassType

object ParseField {

  def apply(
    node: scala.xml.Node,
    definedIn: ClassType): Field = {

    val name = node.label
    val typeString = node.getAttribString("type").getOrElse(throw new RuntimeException(s"Missing type attribute for field ${name}"))
    val typ = ParseFieldType(typeString)
    val flagString = node.getAttribString("flags").getOrElse("")
    val idOverride = node.getAttribString("id").map(java.lang.Short.decode(_).shortValue()).getOrElse(CRC16.calc(name))
    val defaultValue = node.text match {
      case s: String if (s.trim.nonEmpty) => new IdlDefaultValue(s.trim, definedIn)
      case _ => null
    }

    new Field(
      definedIn.fullName,
      name,
      typ,
      if (flagString.size > 0) flagString.split(',').map(_.trim).toSeq else Seq.empty[String],
      idOverride,
      defaultValue)

  }

}