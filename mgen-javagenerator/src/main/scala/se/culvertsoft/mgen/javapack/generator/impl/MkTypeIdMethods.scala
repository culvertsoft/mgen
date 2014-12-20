package se.culvertsoft.mgen.javapack.generator.impl

import se.culvertsoft.mgen.api.model.ClassType
import se.culvertsoft.mgen.api.model.Module
import se.culvertsoft.mgen.compiler.util.SourceCodeBuffer

object MkTypeIdMethods {

  def apply(t: ClassType, module: Module)(implicit txtBuffer: SourceCodeBuffer) {

    implicit val m = module

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public long _typeId() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_ID;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public short _typeId16Bit() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_ID_16BIT;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public String _typeId16BitBase64() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_ID_16BIT_BASE64;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public String _typeName() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_NAME;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public long[] _typeIds() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_IDS;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public short[] _typeIds16Bit() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_IDS_16BIT;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public String[] _typeIds16BitBase64() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_IDS_16BIT_BASE64;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public String _typeIds16BitBase64String() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_IDS_16BIT_BASE64_STRING;")
    txtBuffer.tabs(1).textln("}").endl()

    txtBuffer.tabs(1).textln("@Override")
    txtBuffer.tabs(1).textln("public String[] _typeNames() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_NAMES;")
    txtBuffer.tabs(1).textln("}").endl()

  }
}