package se.culvertsoft.mgen.javapack.serialization;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;

public abstract class BuiltInReader implements Reader {

	protected final ClassRegistry m_classRegistry;

	public BuiltInReader(final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
	}
}
