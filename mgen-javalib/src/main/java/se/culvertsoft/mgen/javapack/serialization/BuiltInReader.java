package se.culvertsoft.mgen.javapack.serialization;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public abstract class BuiltInReader implements Reader {

	private final ClassRegistry m_classRegistry;

	public BuiltInReader(final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
	}

	protected MGenBase instantiate(final short[] typeIds) {

		for (int i = (typeIds.length - 1); i >= 0; i--) {
			final ClassRegistryEntry classRegEntry = m_classRegistry
					.getBy16bitHash(typeIds[i]);
			if (classRegEntry != null)
				return classRegEntry.construct();
		}

		// TODO: Handle failed constraint

		return null;
	}

}
