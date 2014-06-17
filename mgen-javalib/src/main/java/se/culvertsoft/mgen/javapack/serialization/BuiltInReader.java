package se.culvertsoft.mgen.javapack.serialization;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public abstract class BuiltInReader implements Reader {

	private final ClassRegistry m_classRegistry;

	public BuiltInReader(final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
	}

	protected MGenBase instantiate(final short[] globalTypeIds) {

		final int localTypeId = m_classRegistry.globalIds2Local(globalTypeIds);
		final ClassRegistryEntry entry = m_classRegistry
				.getByLocalId(localTypeId);

		if (entry != null)
			return entry.construct();

		return null;
	}

}
