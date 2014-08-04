package se.culvertsoft.mgen.idlparser

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap
import scala.collection.JavaConversions.seqAsJavaList

import se.culvertsoft.mgen.api.model.ParsedSources
import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.model.impl.ParsedSourcesImpl
import se.culvertsoft.mgen.api.plugins.Parser

class IdlParser extends Parser {

  override def parse(
    sources: java.util.List[File],
    settings: java.util.Map[String, String],
    parent: Project): ParsedSources = {
    val out = new ParsedSourcesImpl()
    out.setModules(sources.map(source => ParseModule(source, settings.toMap, parent)))
    out
  }

}