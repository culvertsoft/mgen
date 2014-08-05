package se.culvertsoft.mgen.cpppack.generator.impl.classcpp

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppGenerator
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.get
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.getMutable
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.isSetName

object MkGetters {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields()) {
      txtBuffer.tabs(0).textln(s"const ${getTypeName(field)}& ${t.shortName()}::${get(field)} const {")
      txtBuffer.tabs(1).textln(s"return m_${field.name()};")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

    for (field <- t.fields()) {
      txtBuffer.tabs(0).textln(s"${getTypeName(field)}& ${t.shortName()}::${getMutable(field)} {")
      if (!CppGenerator.canBeNull(field))
        txtBuffer.tabs(1).textln(s"${isSetName(field)} = true;")
      txtBuffer.tabs(1).textln(s"return m_${field.name()};")
      txtBuffer.tabs(0).textln(s"}")
      txtBuffer.endl()
    }

  }

}