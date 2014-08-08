package se.culvertsoft.mgen.idlparser

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.ItemLookup
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue

class IdlDefaultValue(json: String, referencedFrom: ClassType) extends UnlinkedDefaultValue(referencedFrom) {

  override def parse(
    fieldType: Type,
    lookup: ItemLookup): DefaultValue = {
    ParseDefaultValue(fieldType, json, referencedFrom)(lookup)
  }

}