package se.culvertsoft.mgen.javapack.generator.makers

import se.culvertsoft.mgen.api.model.CustomType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import scala.collection.JavaConversions._
import se.culvertsoft.mgen.javapack.generator.JavaConstruction._
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames._
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.javapack.generator.JavaToString

object MkTypeIdFields {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

    implicit val m = module

    val allTypes = t.superTypeHierarchy().map(_.asInstanceOf[CustomType])

    val allTypeIds = allTypes.map(typeIdStr)
    val allTypeIds16Bit = allTypes.map(typeIdStr16bit)
    val allTypeIds16BitBase64 = allTypes.map(typeIdStr16BitBase64)
    val allNames = allTypes.map(name)

    ln(1, s"public static final long _TYPE_ID = ${t.typeId}L;").endl()
    ln(1, s"public static final long[] _TYPE_IDS = { ${allTypeIds.mkString(", ")} };").endl()

    ln(1, s"public static final short _TYPE_ID_16BIT = ${t.typeId16Bit};").endl()
    ln(1, s"public static final short[] _TYPE_IDS_16BIT = { ${allTypeIds16Bit.mkString(", ")} };").endl()

    ln(1, s"public static final String _TYPE_ID_16BIT_BASE64 = ${quote(t.typeId16BitBase64)};").endl()
    ln(1, s"public static final String[] _TYPE_IDS_16BIT_BASE64 = { ${allTypeIds16BitBase64.mkString(", ")} };").endl()

    ln(1, s"public static final String _TYPE_NAME = ${quote(t.fullName())};").endl()
    ln(1, s"public static final String[] _TYPE_NAMES = { ${allNames.mkString(", ")} };").endl()

  }
}