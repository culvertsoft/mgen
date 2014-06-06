package se.culvertsoft.mgen.javapack.classes;

public class ClassRegistryEntry {

	public ClassRegistryEntry(
			final String className,
			final short typeHash16bit,
			final int typeHash32bit,
			final Class<?> cls,
			final Ctor ctor) {
		m_clsName = className;
		m_typeHash16bit = typeHash16bit;
		m_typeHash32bit = typeHash32bit;
		m_class = cls;
		m_ctor = ctor;
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

	public int typeHash32bit() {
		return m_typeHash32bit;
	}

	public Class<?> cls() {
		return m_class;
	}

	public Ctor ctor() {
		return m_ctor;
	}

	private final String m_clsName;
	private final short m_typeHash16bit;
	private final int m_typeHash32bit;
	private final Class<?> m_class;
	private final Ctor m_ctor;

}