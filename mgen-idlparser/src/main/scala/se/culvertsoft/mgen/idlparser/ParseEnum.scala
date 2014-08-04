package se.culvertsoft.mgen.idlparser

import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.impl.EnumTypeImpl
import se.culvertsoft.mgen.api.model.impl.ModuleImpl

object ParseEnum {

  def apply(node: scala.xml.Node, module: ModuleImpl): EnumTypeImpl = {

    val name = node.label
    val fullName = s"${module.path}.$name"

    val enumeration = new EnumTypeImpl(name, fullName, module)

    val entries = node.child.map { ParseEnumEntry(_, module) }

    enumeration.setEntries(entries)
    enumeration
  }

}