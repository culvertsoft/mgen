package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.mapAsScalaMap

import Alias.instantiate
import Alias.name
import Alias.typeIdStr
import Alias.typeIdStr16BitBase64
import Alias.typeIdStr16bit
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.javapack.generator.JavaConstants.clsRegistryClsString

object MkClassRegistry {

  def apply(referencedModules: Seq[Module], packagePath: String)(implicit txtBuffer: SuperStringBuffer): String = {

    txtBuffer.clear()

    MkPackage(packagePath)

    MkClassStart("ClassRegistry", clsRegistryClsString)

    txtBuffer.tabs(1).textln("public ClassRegistry() {")
    for (module <- referencedModules) {
      val fullRegistryClassName = s"${module.path()}.MGenModuleClassRegistry"
      txtBuffer.tabs(2).textln(s"add(new ${fullRegistryClassName}());")
    }
    txtBuffer.tabs(1).textln("}")
    txtBuffer.endl()

    val allTypes = referencedModules.flatMap(_.types()).map(_._2).distinct
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    def mkFunc(
      defaultVal: String,
      returnType: String,
      funcName: String,
      inpTypeStr: String,
      caser: CustomType => String,
      returner: CustomType => String) {
      txtBuffer.tabs(1).textln(s"@Override")
      txtBuffer.tabs(1).textln(s"public $returnType $funcName(final $inpTypeStr[] ids) {")
      txtBuffer.tabs(2).textln("int i = 0;")

      MkTypeIdSwitch(2, defaultVal, topLevelTypes, caser, returner)

      txtBuffer.tabs(1).textln("}").endl()
    }

    def mkLkupFunc(funcName: String, inpTypeStr: String, caser: CustomType => String) {
      mkFunc("-1", "long", funcName, inpTypeStr, caser, typeIdStr)
    }

    def mkInstantiateFunc(funcName: String, inpTypeStr: String, caser: CustomType => String) {
      mkFunc("null", JavaConstants.mgenBaseClsString, funcName, inpTypeStr, caser, instantiate)
    }

    mkLkupFunc("typeIds16Bit2TypeId", "short", typeIdStr16bit)
    mkLkupFunc("typeIds16Base64Bit2TypeId", "String", typeIdStr16BitBase64)
    mkLkupFunc("names2TypeId", "String", name)

    mkInstantiateFunc("instantiateByTypeIds16Bit", "short", typeIdStr16bit)
    mkInstantiateFunc("instantiateByTypeIds16BitBase64", "String", typeIdStr16BitBase64)
    mkInstantiateFunc("instantiateByNames", "String", name)

    MkClassEnd()

    txtBuffer.toString()
  }
}