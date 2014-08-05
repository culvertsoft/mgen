package se.culvertsoft.mgen.idlparser

import se.culvertsoft.mgen.api.model.EnumEntry
import se.culvertsoft.mgen.api.model.Module

object ParseEnumEntry {

  def apply(node: scala.xml.Node, module: Module): EnumEntry = {
    val name = node.label
    val constant = if (node.text != null && node.text.trim.nonEmpty) node.text else null
    new EnumEntry(name, constant)
  }

}