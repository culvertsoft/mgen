package se.culvertsoft.mgen.idlparser

import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.ItemLookup

class IdlDefaultValue(json: String) extends UnlinkedDefaultValue {

  override def parse(
      fieldType: Type, 
      referencedFrom: ClassType,
      lookup: ItemLookup): DefaultValue = {
    ParseDefaultValue(fieldType, json, referencedFrom)(lookup)
  }

}