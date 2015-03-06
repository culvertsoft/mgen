package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil.ln
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkTypeIdMethods {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    ln(1, "@Override")
    ln(1, "public long _typeId() {")
    ln(2, s"return _TYPE_ID;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public short _typeId16Bit() {")
    ln(2, s"return _TYPE_ID_16BIT;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public String _typeId16BitBase64() {")
    ln(2, s"return _TYPE_ID_16BIT_BASE64;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public String _typeName() {")
    ln(2, s"return _TYPE_NAME;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public long[] _typeIds() {")
    ln(2, s"return _TYPE_IDS;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public short[] _typeIds16Bit() {")
    ln(2, s"return _TYPE_IDS_16BIT;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public String[] _typeIds16BitBase64() {")
    ln(2, s"return _TYPE_IDS_16BIT_BASE64;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public String _typeIds16BitBase64String() {")
    ln(2, s"return _TYPE_IDS_16BIT_BASE64_STRING;")
    ln(1, "}").endl()

    ln(1, "@Override")
    ln(1, "public String[] _typeNames() {")
    ln(2, s"return _TYPE_NAMES;")
    ln(1, "}").endl()

  }
}