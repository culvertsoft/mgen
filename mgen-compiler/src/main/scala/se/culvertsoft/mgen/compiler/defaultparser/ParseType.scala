package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ArrayBuffer

import se.culvertsoft.mgen.api.model.UserDefinedType
import se.culvertsoft.mgen.api.model.impl.LinkedCustomType
import se.culvertsoft.mgen.api.model.impl.ModuleImpl
import se.culvertsoft.mgen.api.model.impl.UnlinkedCustomType
import se.culvertsoft.mgen.api.util.CRC16
import se.culvertsoft.mgen.compiler.util.XmlUtils.RichXmlNode

object ParseType {

  def apply(node: scala.xml.Node, module: ModuleImpl)(implicit cache: ParseState): LinkedCustomType = {

    val name = node.label
    val fullName = s"${module.path}.$name"

    val superType =
      node.getAttribString("extends") match {
        case Some(superTypeName) => new UnlinkedCustomType(superTypeName, -1)
        case _ => null
      }

    val id16Bit =
      node.getAttribString("id") match {
        case Some(idString) => java.lang.Short.decode(idString).toShort
        case _ => CRC16.calc(fullName)
      }

    val clas = new LinkedCustomType(name, module, id16Bit, superType)

    val fields = node.child.map { ParseField(_, clas.fullName) }
    clas.setFields(fields)

    cache.typeLookup.typesFullName.put(clas.fullName(), clas)
    cache.typeLookup.typesShortName.getOrElseUpdate(clas.shortName(), new ArrayBuffer[UserDefinedType])
    cache.typeLookup.typesShortName(clas.shortName()) += clas

    if (clas.hasSuperType() || clas.fields.exists(!_.isLinked))
      cache.needLinkage.types += clas

    clas
  }

}