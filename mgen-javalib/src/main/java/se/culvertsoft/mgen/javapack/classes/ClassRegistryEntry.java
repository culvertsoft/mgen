package se.culvertsoft.mgen.javapack.classes;

public class ClassRegistryEntry {

	public ClassRegistryEntry(
			final long typeId, 
			final String className,
			final Ctor ctor) {
		m_cls = lkupClass(className);
		m_typeId = typeId;
		m_clsName = className;
		m_ctor = ctor;
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

	public String clsName() {
		return m_clsName;
	}

	public long typeId() {
		return m_typeId;
	}

	public Ctor ctor() {
		return m_ctor;
	}

	public Class<?> cls() {
		return m_cls;
	}

	private final Class<?> m_cls;
	private final long m_typeId;
	private final String m_clsName;
	private final Ctor m_ctor;

}