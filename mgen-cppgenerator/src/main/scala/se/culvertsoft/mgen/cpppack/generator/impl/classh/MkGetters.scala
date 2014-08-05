package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.get
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.getMutable

object MkGetters {

  def apply(
    t: ClassType,
    module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val currentModule = module

    for (field <- t.fields())
      txtBuffer
        .tabs(1)
        .textln(s"const ${getTypeName(field)}& ${get(field)} const;")

    if (t.fields().nonEmpty)
      txtBuffer.endl()

    for (field <- t.fields())
      txtBuffer
        .tabs(1)
        .textln(s"${getTypeName(field)}& ${getMutable(field)};")

    if (t.fields().nonEmpty)
      txtBuffer.endl()

  }

}