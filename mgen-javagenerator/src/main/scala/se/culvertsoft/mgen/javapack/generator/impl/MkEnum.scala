package se.culvertsoft.mgen.javapack.generator.impl

import scala.collection.JavaConversions.asScalaBuffer
import se.culvertsoft.mgen.api.model.EnumType
import se.culvertsoft.mgen.compiler.internal.BuiltInGeneratorUtil._
import se.culvertsoft.mgen.compiler.util.SuperStringBuffer
import se.culvertsoft.mgen.javapack.generator.JavaConstants
import se.culvertsoft.mgen.api.model.impl.EnumEntryImpl

object MkEnum {

  def apply(_e: EnumType, packagePath: String)(implicit txtBuffer: SuperStringBuffer): String = {

    val name = _e.shortName()
    val entries = _e.entries() ++ List(new EnumEntryImpl("UNKNOWN", null))
    
    txtBuffer.clear()

    MkFancyHeader.apply(null)

    MkPackage(packagePath)

    ln(s"import ${JavaConstants.enumImplClsStringQ};")
    ln(s"import ${JavaConstants.mgenEnumClsString};")
    ln()

    ln(s"public enum $name implements MGenEnum {")

    var curVal = -1

    for (entry <- entries) {
      curVal =
        if (entry.constant() != null)
          java.lang.Integer.decode(entry.constant())
        else
          curVal + 1;
      txt(1, s"${entry.name}($curVal, ${quote(entry.name)})")
      if (entry != entries.last)
        ln(",")
    }
    ln(";")
    ln()

    ln(1, s"final int m_intValue;")
    ln(1, s"final String m_stringValue;")
    ln()
    ln(1, s"$name(final int intValue, final String stringValue) {")
    ln(2, s"m_intValue = intValue;")
    ln(2, s"m_stringValue = stringValue;")
    ln(1, s"}")
    ln()

    ln(1, "@Override")
    ln(1, "public int getIntValue() {")
    ln(2, "return m_intValue;")
    ln(1, "}")
    ln()

    ln(1, "@Override")
    ln(1, "public String getStringValue() {")
    ln(2, "return m_stringValue;")
    ln(1, "}")
    ln()

    ln(1, s"public static final ${JavaConstants.enumImplClsString} _TYPE = new ${JavaConstants.enumImplClsString}(${quote(_e.shortName)}, ${quote(_e.fullName)});")
    
    ln("}")

    txtBuffer.toString()

  }

}