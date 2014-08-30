package se.culvertsoft.mgen.api.model;

/**
 * An unlinked type, as parsed by IDL parsers during the compilers first pass -
 * before linking field types, default values and super types.
 */
public class UnlinkedType extends UserDefinedType {

	/**
	 * Returns the written name of this type. This may be either the short or
	 * full name.
	 */
	public String name() {
		return m_name;
	}

	public UnlinkedType(final String name) {
		super(TypeEnum.UNKNOWN, null);
		m_name = name;
	}

	private final String m_name;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return m_name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return m_name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return null;
	}

}
