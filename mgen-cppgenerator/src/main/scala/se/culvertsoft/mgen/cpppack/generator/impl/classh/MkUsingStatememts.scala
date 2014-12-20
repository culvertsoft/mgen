package se.culvertsoft.mgen.cpppack.generator.impl.classh

import scala.collection.JavaConversions.asScalaBuffer

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkUsingStatements {

  def apply(t: ClassType)(implicit txtBuffer: SourceCodeBuffer) {

    if (t.fields.exists(_.isPolymorphic)) {
      txtBuffer.tabs(0).textln(s"using mgen::Polymorphic;")
      txtBuffer.endl()
    }

  }

}