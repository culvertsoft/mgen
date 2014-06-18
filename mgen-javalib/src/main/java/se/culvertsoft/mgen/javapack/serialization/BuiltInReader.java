package se.culvertsoft.mgen.javapack.serialization;

import se.culvertsoft.mgen.javapack.classes.ClassRegistry;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public abstract class BuiltInReader implements Reader {

	protected final ClassRegistry m_classRegistry;

	public BuiltInReader(final ClassRegistry classRegistry) {
		m_classRegistry = classRegistry;
	}

	protected MGenBase instantiate(final int localId) {
		return m_classRegistry.instantiateFromLocalId(localId);
	}

	protected MGenBase instantiateFromHash16(final short[] ids) {
		return m_classRegistry.instantiateFromHash16Ids(ids);
	}

	protected MGenBase instantiateFromNames(final String[] ids) {
		return m_classRegistry.instantiateFromNames(ids);
	}

	protected MGenBase instantiateFromHash16Base64(final String[] ids) {
		return m_classRegistry.instantiateFromHash16Base64Ids(ids);
	}

}
