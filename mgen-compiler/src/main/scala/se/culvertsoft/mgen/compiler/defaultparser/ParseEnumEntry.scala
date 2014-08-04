package se.culvertsoft.mgen.compiler.defaultparser

import se.culvertsoft.mgen.api.model.impl.EnumEntryImpl
import se.culvertsoft.mgen.api.model.impl.ModuleImpl

object ParseEnumEntry {

  def apply(node: scala.xml.Node, module: ModuleImpl): EnumEntryImpl = {
    val name = node.label
    val constant = if (node.text != null && node.text.trim.nonEmpty) node.text else null
    new EnumEntryImpl(name, constant)
  }

}