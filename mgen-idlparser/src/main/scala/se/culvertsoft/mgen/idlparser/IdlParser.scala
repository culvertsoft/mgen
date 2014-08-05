package se.culvertsoft.mgen.idlparser

import java.io.File

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.JavaConversions.mapAsScalaMap

import se.culvertsoft.mgen.api.model.Project
import se.culvertsoft.mgen.api.plugins.Parser

class IdlParser extends Parser {

  override def parse(
    sources: java.util.List[File],
    settings: java.util.Map[String, String],
    parent: Project) {
    sources.foreach(ParseModule(_, settings.toMap, parent))
  }

}