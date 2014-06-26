package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer

import scala.collection.JavaConversions._

object MkOutro {

  def apply(settings: java.util.Map[String, String])(implicit txtBuffer: SuperStringBuffer) = {
    val classregistryName = settings.getOrElse("classregistry_name", "mgen_registry")
    ln("})(" + classregistryName + ");")
  }
}