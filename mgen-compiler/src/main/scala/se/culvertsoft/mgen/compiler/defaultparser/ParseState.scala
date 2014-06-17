package se.culvertsoft.mgen.compiler.defaultparser

import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap

import se.culvertsoft.mgen.api.model.impl.CustomTypeImpl
import se.culvertsoft.mgen.api.model.impl.ModuleImpl
import se.culvertsoft.mgen.api.model.impl.ProjectImpl

class ParseState {

  object fileParsing {
    val projects = new HashMap[String, ProjectImpl]
    val modules = new HashMap[String, ModuleImpl]
  }

  object typeLookup {
    val typesShortName = new HashMap[String, ArrayBuffer[CustomTypeImpl]]
    val typesFullName = new HashMap[String, CustomTypeImpl]
  }

  object needLinkage {
    val types = new ArrayBuffer[CustomTypeImpl]
  }

  private var _nextLocalId = 0

  def nextLocalId() = synchronized {
    val out = _nextLocalId
    _nextLocalId += 1
    out
  }

}