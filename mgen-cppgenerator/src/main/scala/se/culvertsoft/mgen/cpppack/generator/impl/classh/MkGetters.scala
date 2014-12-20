package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.cpppack.generator.CppTypeNames.getTypeName
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.get
import se.culvertsoft.mgen.cpppack.generator.impl.Alias.getMutable

object MkGetters {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val module = t.module

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