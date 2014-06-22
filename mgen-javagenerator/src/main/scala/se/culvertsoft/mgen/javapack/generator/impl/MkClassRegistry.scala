package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.mapAsScalaMap

import Alias.instantiate
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

    MkFancyHeader.apply(null)
    
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
      transform: String => String,
      oobString: String,
      defaultVal: String,
      returnType: String,
      funcName: String,
      inpTypeStr: String,
      caser: CustomType => String,
      returner: CustomType => String) {
      txtBuffer.tabs(1).textln(s"@Override")
      txtBuffer.tabs(1).textln(s"public $returnType $funcName(final $inpTypeStr[] ids) {")
      txtBuffer.tabs(2).textln("int i = 0;")

      MkTypeIdSwitch(transform, oobString, false, 2, defaultVal, topLevelTypes, caser, returner)

      txtBuffer.tabs(1).textln("}").endl()
    }

    def mkLkupFunc(transform: String => String, oobString: String, funcName: String, inpTypeStr: String, caser: CustomType => String) {
      mkFunc(transform, oobString, "return -1;", "long", funcName, inpTypeStr, caser, s => s"return ${typeIdStr(s)};")
    }

    def mkInstantiateFunc(transform: String => String, oobString: String, funcName: String, inpTypeStr: String, caser: CustomType => String) {
      mkFunc(transform, oobString, "return null;", JavaConstants.mgenBaseClsString, funcName, inpTypeStr, caser, s => s"return ${instantiate(s)};")
    }

    mkLkupFunc(s => s"(int)$s", "0xFFFFFFFF", "typeIds16Bit2TypeId", "short", typeIdStr16bit)
    mkLkupFunc(s => s, "\"0xFFFFFFFF\"", "typeIds16Base64Bit2TypeId", "String", typeIdStr16BitBase64)

    mkInstantiateFunc(s => s"(int)$s", "0xFFFFFFFF", "instantiateByTypeIds16Bit", "short", typeIdStr16bit)
    mkInstantiateFunc(s => s, "\"0xFFFFFFFF\"", "instantiateByTypeIds16BitBase64", "String", typeIdStr16BitBase64)

    MkClassEnd()

    txtBuffer.toString()
  }
}