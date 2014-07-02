package se.culvertsoft.mgen.javapack.classes;

import se.culvertsoft.mgen.api.model.UnknownCustomType;
import se.culvertsoft.mgen.api.model.impl.UnknownCustomTypeImpl;

public class ClassRegistryEntry {

	public ClassRegistryEntry(
			final long typeId,
			final long[] typeIds, 
			final String className, 
			final Ctor ctor) {
		m_cls = lkupClass(className);
		m_typeId = typeId;
		m_typeIds = typeIds;
		m_clsName = className;
		m_ctor = ctor;
		m_type = new UnknownCustomTypeImpl(m_clsName, typeId);
	}

	private static Class<?> lkupClass(final String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Failed to init class " + className, e);
		}
	}

	public MGenBase construct() {
		return m_ctor.create();
	}

	public long typeId() {
		return m_typeId;
	}
	
	public long[] typeIds() {
		return m_typeIds;
	}

	public String clsName() {
		return m_clsName;
	}

	public Ctor ctor() {
		return m_ctor;
	}

	public Class<?> cls() {
		return m_cls;
	}
	
	public UnknownCustomType typ() {
		return m_type;
	}

	public boolean isInstanceOfTypeId(final long typeId) {
		for (int i = m_typeIds.length - 1; i >= 0; i--) {
			if (m_typeIds[i] == typeId) {
				return true;
			}
		}
		return false;
	}

	private final Class<?> m_cls;
	private final long m_typeId;
	private final long[] m_typeIds;
	private final String m_clsName;
	private final Ctor m_ctor;
	private final UnknownCustomType m_type;

}