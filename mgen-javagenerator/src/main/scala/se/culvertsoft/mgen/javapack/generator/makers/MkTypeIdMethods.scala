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

object MkTypeIdMethods {
  import BuiltInGeneratorUtil._
  import JavaConstants._
  import Alias._
  import JavaToString._

  def apply(t: CustomType, module: Module)(implicit txtBuffer: SuperStringBuffer) {

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
    txtBuffer.tabs(1).textln("public String[] _typeNames() {")
    txtBuffer.tabs(2).textln(s"return _TYPE_NAMES;")
    txtBuffer.tabs(1).textln("}").endl()
    
  }
}