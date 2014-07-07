package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.seqAsJavaList
import scala.collection.mutable.ArrayBuffer
import XmlUtils.RichXmlNode
import se.culvertsoft.mgen.api.model.MGenBaseType
import se.culvertsoft.mgen.api.model.impl.CustomTypeImpl
import se.culvertsoft.mgen.api.model.impl.ModuleImpl
import se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl
import scala.util.Try
import scala.util.Success

object ParseType {

  def apply(node: scala.xml.Node, module: ModuleImpl)(implicit cache: ParseState): CustomTypeImpl = {

    val name = node.label

    val superType =
      node.getAttribString("extends") match {
        case Some(superTypeName) => new UnknownCustomTypeImpl(superTypeName, -1)
        case _ => MGenBaseType.INSTANCE
      }

    val clas = new CustomTypeImpl(name, module, superType)

    node.getAttribString("id") match {
      case Some(idString) => clas.override16BitId(java.lang.Short.decode(idString))
      case _ =>
    }

    val fields = node.child.map { ParseField(_, clas.fullName) }
    clas.setFields(fields)

    cache.typeLookup.typesFullName.put(clas.fullName(), clas)
    cache.typeLookup.typesShortName.getOrElseUpdate(clas.shortName(), new ArrayBuffer[CustomTypeImpl])
    cache.typeLookup.typesShortName(clas.shortName()) += clas

    if (clas.superType() != MGenBaseType.INSTANCE || clas.fields().exists(!_.typ().isTypeKnown()))
      cache.needLinkage.types += clas

    clas
  }

}