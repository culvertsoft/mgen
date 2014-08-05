package se.culvertsoft.mgen.idlparser

import se.culvertsoft.mgen.api.model.UnlinkedDefaultValue
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.DefaultValue
import se.culvertsoft.mgen.api.model.Module

class IdlDefaultValue(json: String) extends UnlinkedDefaultValue {

  override def parse(fieldType: Type, parentModule: Module): DefaultValue = {
    ParseDefaultValue(fieldType, json, parentModule)
  }

}