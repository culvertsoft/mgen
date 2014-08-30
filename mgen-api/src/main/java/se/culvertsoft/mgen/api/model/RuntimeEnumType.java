package se.culvertsoft.mgen.api.model;

/**
 * Represents an enumeration
 */
public abstract class RuntimeEnumType extends UserDefinedType {

	/**
	 * Gets enum instances by name.
	 */
	public abstract Enum<?> get(final String entryStringName);

	/**
	 * Gets enum instances by integer.
	 */
	public abstract Enum<?> get(final int entryIntvalue);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return m_shortName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return m_fullName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return false;
	}

	/**
	 * The class of this type, intended to be used for type introspection during
	 * runtime - not from within the compiler.
	 */
	public Class<?> classOf() {
		return m_cls;
	}

	private final String m_shortName;
	private final String m_fullName;
	private final Class<?> m_cls;

	public RuntimeEnumType(final String shortName, final String fullName) {
		super(TypeEnum.ENUM, null);
		m_shortName = shortName;
		m_fullName = fullName;
		m_cls = getClass(fullName);
	}

	private static Class<?> getClass(final String fullName) {
		try {
			return Class.forName(fullName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
