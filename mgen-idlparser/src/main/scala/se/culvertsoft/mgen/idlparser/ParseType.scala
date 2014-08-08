package se.culvertsoft.mgen.idlparser

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.UnlinkedType
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.idlparser.util.XmlUtils.RichXmlNode

object ParseType {

  def apply(node: scala.xml.Node, module: Module): ClassType = {

    val name = node.label
    val fullName = s"${module.path}.$name"

    val superType =
      node.getAttribString("extends") match {
        case Some(superTypeName) => new UnlinkedType(superTypeName)
        case _ => null
      }

    val id16Bit =
      node.getAttribString("id") match {
        case Some(idString) => java.lang.Short.decode(idString).toShort
        case _ => CRC16.calc(fullName)
      }

    val clas = new ClassType(name, module, id16Bit, superType)

    val fields = node.child.map { ParseField(_, clas) }
    clas.setFields(fields)

    clas
  }

}