package se.culvertsoft.mgen.idlparser

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.api.model.Module

object ParseEnum {

  def apply(node: scala.xml.Node, module: Module): EnumType = {

    val name = node.label
    val fullName = s"${module.path}.$name"

    val enumeration = new EnumType(name, fullName, module)

    val entries = node.child.map { ParseEnumEntry(_, module) }

    enumeration.setEntries(entries)
    enumeration
  }

}