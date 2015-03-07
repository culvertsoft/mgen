package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer

import Alias.get
import Alias.getCopy
import Alias.isFieldSet
import se.culvertsoft.mgen.api.model.ArrayType
import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.ListType
import se.culvertsoft.mgen.api.model.MapType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.api.model.Type
import se.culvertsoft.mgen.api.model.TypeEnum
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.upFirst
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants.fieldSetDepthClsString
import se.culvertsoft.mgen.javapack.generator.JavaConstruction.constructArray
import se.culvertsoft.mgen.javapack.generator.JavaConstruction.constructList
import se.culvertsoft.mgen.javapack.generator.JavaConstruction.constructMap
import se.culvertsoft.mgen.javapack.generator.JavaGenerator.isMutable
import se.culvertsoft.mgen.javapack.generator.JavaTypeNames.declared

object MkGetters {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    for (field <- t.fields()) {
      ln(1, s"public ${declared(field)} ${get(field)} {")
      ln(2, s"return m_${field.name()};")
      ln(1, s"}").endl()
    }

    for (field <- t.fields()) {
      ln(1, s"public boolean has${upFirst(field.name())}() {")
      ln(2, s"return ${isFieldSet(field, s"${fieldSetDepthClsString}.SHALLOW")};")
      ln(1, s"}").endl()
    }

    for (field <- t.fieldsInclSuper()) {
      ln(1, s"public ${declared(t, false)} unset${upFirst(field.name())}() {")
      ln(2, s"_set${upFirst(field.name())}Set(false, ${fieldSetDepthClsString}.SHALLOW);")
      ln(2, s"return this;")
      ln(1, s"}").endl()
    }

    for (field <- t.fields()) {
      ln(1, s"public ${declared(field)} ${getCopy(field)} {")
      if (isMutable(field)) {
        ln(2, s"${declared(field)} out = null;")
        cpExpr("out", get(field), 2, module, field.typ, false, new VarNamer)
        ln(2, s"return out;")
      } else {
        ln(2, s"return m_${field.name()};")
      }
      ln(1, s"}").endl()
    }

  }

  class VarNamer {
    def next(): String = {
      val out = "var" + i
      i += 1
      out
    }
    private var i = 0
  }

  private def cpExpr(
    trgVar: String,
    _srcVar: String,
    tabLevel: Int,
    module: Module,
    typ: Type,
    genericTypeArg: Boolean,
    namer: VarNamer)(implicit b: SourceCodeBuffer) {

    implicit val m = module

    // To avoid double get
    val srcVar = namer.next()
    val typString = declared(typ, genericTypeArg)

    if (isMutable(typ)) {
      ln(tabLevel, s"final $typString $srcVar = ${_srcVar};")
      ln(tabLevel, s"if ($srcVar != null) {")
    }

    // Not null
    typ.typeEnum match {
      case TypeEnum.MAP =>
        val mapType = typ.asInstanceOf[MapType]
        val keyType = mapType.keyType
        val valueType = mapType.valueType
        val keyTypString = declared(keyType, true)
        val valueTypString = declared(valueType, true)
        val srcElemName = namer.next()
        val trgKeyName = namer.next()
        val trgValueName = namer.next()
        ln(tabLevel + 1, s"$trgVar = ${constructMap(mapType, s"$srcVar.size()")};")
        ln(tabLevel + 1, s"for (final java.util.Map.Entry<$keyTypString, $valueTypString> $srcElemName : $srcVar.entrySet()) {")
        ln(tabLevel + 2, s"$keyTypString $trgKeyName = null;")
        ln(tabLevel + 2, s"$valueTypString $trgValueName = null;")
        cpExpr(trgKeyName, s"$srcElemName.getKey()", tabLevel + 2, module, keyType, true, namer);
        cpExpr(trgValueName, s"$srcElemName.getValue()", tabLevel + 2, module, valueType, true, namer);
        ln(tabLevel + 2, s"$trgVar.put($trgKeyName, $trgValueName);")
        ln(tabLevel + 1, s"}")
      case TypeEnum.ARRAY =>
        val arrayType = typ.asInstanceOf[ArrayType]
        val elemType = arrayType.elementType
        val elemTypString = declared(elemType, false)
        val loopIndex = namer.next()
        ln(tabLevel + 1, s"$trgVar = ${constructArray(arrayType, s"$srcVar.length", false)};")
        ln(tabLevel + 1, s"for (int $loopIndex = 0; $loopIndex < $srcVar.length; $loopIndex++) {")
        cpExpr(s"$trgVar[$loopIndex]", s"$srcVar[$loopIndex]", tabLevel + 2, module, elemType, false, namer);
        ln(tabLevel + 1, s"}")
      case TypeEnum.LIST =>
        val listType = typ.asInstanceOf[ListType]
        val elemType = listType.elementType
        val elemTypString = declared(elemType, true)
        val srcElemName = namer.next()
        val trgElemname = namer.next()
        ln(tabLevel + 1, s"$trgVar = ${constructList(listType, s"$srcVar.size()")};")
        ln(tabLevel + 1, s"for (final $elemTypString $srcElemName : $srcVar) {")
        ln(tabLevel + 2, s"$elemTypString $trgElemname = null;")
        cpExpr(trgElemname, srcElemName, tabLevel + 2, module, elemType, true, namer);
        ln(tabLevel + 2, s"$trgVar.add($trgElemname);")
        ln(tabLevel + 1, s"}")
      case TypeEnum.CLASS =>
        ln(tabLevel + 1, s"$trgVar = $srcVar.deepCopy();")
      case _ =>
        ln(tabLevel, s"$trgVar = ${_srcVar};")
    }

    if (isMutable(typ)) {
      ln(tabLevel, s"} else {")
      ln(tabLevel + 1, s"$trgVar = null;")
      ln(tabLevel, s"}")
    }

  }

}