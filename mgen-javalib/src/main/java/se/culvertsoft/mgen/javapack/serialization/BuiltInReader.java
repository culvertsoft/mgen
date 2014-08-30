package se.culvertsoft.mgen.javapack.serialization;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.util.StringDecoder;

/**
 * Base class for all built-in MGen wire format readers - not intended to be
 * used directly. See BinaryReader and JsonReader instead.
 */
abstract public class BuiltInReader implements Reader {

	public static final ByteArrayInputStream EMPTY_INPUT_STREAM = new ByteArrayInputStream(
			new byte[0]);
	public static final Charset CHARSET_UTF8 = Charset.forName("UTF8");
	public static final int STRING_DECODE_BUFFER_SIZE = 256;

	protected final ClassRegistryBase m_clsReg;
	protected final StringDecoder m_stringDecoder;

	protected BuiltInReader(ClassRegistryBase classRegistry) {
		m_clsReg = classRegistry;
		m_stringDecoder = new StringDecoder(STRING_DECODE_BUFFER_SIZE, CHARSET_UTF8);
	}

}
