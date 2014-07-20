package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.UserDefinedType
import se.culvertsoft.mgen.api.model.impl.EnumTypeImpl
import se.culvertsoft.mgen.api.model.impl.ModuleImpl

object ParseEnum {

  def apply(node: scala.xml.Node, module: ModuleImpl)(implicit cache: ParseState): EnumTypeImpl = {

    val name = node.label
    val fullName = s"${module.path}.$name"

    val enumeration = new EnumTypeImpl(name, fullName, module)

    val entries = node.child.map { ParseEnumEntry(_, module) }

    enumeration.setEntries(entries)

    cache.typeLookup.typesFullName.put(enumeration.fullName(), enumeration)
    cache.typeLookup.typesShortName.getOrElseUpdate(enumeration.shortName(), new ArrayBuffer[UserDefinedType])
    cache.typeLookup.typesShortName(enumeration.shortName()) += enumeration

    enumeration
  }

}