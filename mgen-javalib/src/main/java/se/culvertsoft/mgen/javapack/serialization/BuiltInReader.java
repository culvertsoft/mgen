package se.culvertsoft.mgen.javapack.serialization;

import java.io.DataInputStream;
import java.nio.charset.Charset;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;

abstract public class BuiltInReader implements Reader {

	protected static final Charset charset = Charset.forName("UTF8");
	protected static final short[] NO_IDS = new short[0];

	protected final DataInputStream m_stream;
	protected final ClassRegistry m_classRegistry;

	protected BuiltInReader(DataInputStream stream, ClassRegistry classRegistry) {
		m_stream = stream;
		m_classRegistry = classRegistry;
	}

}
