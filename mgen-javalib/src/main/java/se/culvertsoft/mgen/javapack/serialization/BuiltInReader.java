package se.culvertsoft.mgen.javapack.serialization;

import java.nio.charset.Charset;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;
import se.culvertsoft.mgen.javapack.util.StringDecoder;

abstract public class BuiltInReader implements Reader {

	public static final Charset CHARSET = Charset.forName("UTF8");
	public static final int STRING_DECODE_BUFFER_SIZE = 256;

	protected final ClassRegistryBase m_clsReg;
	protected final StringDecoder m_stringDecoder;

	protected BuiltInReader(ClassRegistryBase classRegistry) {
		m_clsReg = classRegistry;
		m_stringDecoder = new StringDecoder(STRING_DECODE_BUFFER_SIZE, CHARSET);
	}

}
