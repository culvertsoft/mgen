package se.culvertsoft.mgen.jspack.generator

import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import scala.collection.JavaConversions._

object MkModuleHashRegistry {

  def apply(modules: Seq[Module])(implicit txtBuffer: SuperStringBuffer) {

    val allClasses = modules.flatMap(_.classes).distinct
    val topLevelClasses = allClasses.filterNot(_.hasSuperType)
    txtBuffer {
      scopeExt("blueprint.lookup = function( typeId ) ", ";") {
        ln("var t = typeId.match(/(.{1,3})/g); // Split typeId into array for easier reading of inheritance (every 3 char is a type).")
        MkHashSwitch(topLevelClasses)
      }
    }
  }
}