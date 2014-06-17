package se.culvertsoft.mgen.javapack.serialization;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.ClassRegistryEntry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public abstract class BuiltInReader implements Reader {

	protected final ClassRegistry m_classRegistry;

	public BuiltInReader(final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
	}

	protected MGenBase instantiate(final int localId) {
		final ClassRegistryEntry entry = m_classRegistry.getByLocalId(localId);
		return entry != null ? entry.construct() : null;
	}

	protected MGenBase instantiateFromHash16(final short[] ids) {
		return instantiate(m_classRegistry.globalIds2Local(ids));
	}

	protected MGenBase instantiateFromNames(final String[] ids) {
		return instantiate(m_classRegistry.globalNames2Local(ids));
	}

	protected MGenBase instantiateFromHash16Base64(final String[] ids) {
		return instantiate(m_classRegistry.globalBase64Ids2Local(ids));
	}
	
}
