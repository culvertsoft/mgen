package se.culvertsoft.mgen.javapack.classes;

public class ClassRegistryEntry {

	public ClassRegistryEntry(
			final int localTypeId, 
			final String className,
			final short typeHash16bit, 
			final Ctor ctor) {
		m_cls = lkupClass(className);
		m_localTypeId = localTypeId;
		m_clsName = className;
		m_typeHash16bit = typeHash16bit;
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

	public short typeHash16bit() {
		return m_typeHash16bit;
	}

	public int localTypeId() {
		return m_localTypeId;
	}

	public Ctor ctor() {
		return m_ctor;
	}

	public Class<?> cls() {
		return m_cls;
	}

	private final Class<?> m_cls;
	private final int m_localTypeId;
	private final String m_clsName;
	private final short m_typeHash16bit;
	private final Ctor m_ctor;

}