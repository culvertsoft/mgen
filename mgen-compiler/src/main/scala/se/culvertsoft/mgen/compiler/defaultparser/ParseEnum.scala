package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ArrayBuffer
import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.impl.LinkedCustomType
import se.culvertsoft.mgen.api.model.impl.ModuleImpl
import se.culvertsoft.mgen.api.model.impl.UnlinkedCustomType
import scala.util.Try
import scala.util.Success
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.api.model.impl.EnumTypeImpl
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.UserDefinedType

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