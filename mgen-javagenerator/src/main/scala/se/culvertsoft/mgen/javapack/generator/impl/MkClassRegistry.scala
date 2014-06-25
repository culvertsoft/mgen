package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions._
import Alias.instantiate
import Alias.typeIdStr
import Alias.typeIdStr16BitBase64
import Alias.typeIdStr16bit
import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants._
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._

object MkClassRegistry {

  def getEntryName(t: CustomType): String = {
    t.fullName().replaceAllLiterally(".", "_")
  }

  def mkEntry(t: CustomType): String = {
    s"new $clsRegistryEntryClsString()"
  }

  def getId(t: CustomType): String = {
    s"${t.typeId()}L"
  }

  def apply(referencedModules: Seq[Module], packagePath: String)(implicit txtBuffer: SuperStringBuffer): String = {

    val allTypes = referencedModules.flatMap(_.types()).map(_._2).distinct
    val topLevelTypes = allTypes.filterNot(_.hasSuperType())

    txtBuffer.clear()

    MkFancyHeader.apply(null)

    MkPackage(packagePath)

    ln(s"import ${clsRegistryEntryClsStringQ};")
    ln("import se.culvertsoft.mgen.javapack.classes.Ctor;")
    ln("import se.culvertsoft.mgen.javapack.classes.MGenBase;")
    endl()

    MkClassStart("ClassRegistry", clsRegistryClsString)
    for (t <- allTypes) {
      val id = getId(t)
      val ids = s"${t.fullName()}._TYPE_IDS"
      val name = s"${quote(t.fullName())}"
      val ctor = s"new Ctor() { public MGenBase create() { return new ${t.fullName()}(); } }"
      ln(1, s"public static $clsRegistryEntryClsString ${getEntryName(t)} = new $clsRegistryEntryClsString($id, $ids, $name, $ctor);")
    }
    endl()

    txtBuffer.tabs(1).textln("public ClassRegistry() {")
    for (t <- allTypes) {
      ln(2, s"add(${getEntryName(t)});")
    }
    txtBuffer.tabs(1).textln("}")
    txtBuffer.endl()

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

    def mkInstantiateFunc(transform: String => String, oobString: String, funcName: String, inpTypeStr: String, caser: CustomType => String) {
      mkFunc(transform, oobString, "return null;", clsRegistryEntryClsString, funcName, inpTypeStr, caser, t => s"return ${getEntryName(t)};")
    }

    mkInstantiateFunc(s => s"(int)$s", "0xFFFFFFFF", "getByTypeIds16Bit", "short", typeIdStr16bit)
    mkInstantiateFunc(s => s, "\"0xFFFFFFFF\"", "getByTypeIds16BitBase64", "String", typeIdStr16BitBase64)

    MkClassEnd()

    txtBuffer.toString()
  }
}