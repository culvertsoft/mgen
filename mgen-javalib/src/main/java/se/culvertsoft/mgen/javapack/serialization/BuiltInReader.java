package se.culvertsoft.mgen.javapack.serialization;

import java.nio.charset.Charset;

import se.culvertsoft.mgen.javapack.classes.ClassRegistryBase;

abstract public class BuiltInReader implements Reader {

	protected static final Charset CHARSET = Charset.forName("UTF8");

	protected final ClassRegistryBase m_clsReg;

	protected BuiltInReader(ClassRegistryBase classRegistry) {
		m_clsReg = classRegistry;
	}

}
