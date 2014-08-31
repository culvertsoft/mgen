package se.culvertsoft.mgen.api.model;

/**
 * This type is used in generated source code to specify field types, primarily
 * for serializers/deserializers to use.
 */
public class RuntimeClassType extends UserDefinedType {

	/**
	 * 64 bit id hashed from the qualified type name of this class
	 */
	public long typeId() {
		return m_id;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLinked() {
		return true;
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
	public String shortName() {
		return fullName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return m_cls;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return m_fullName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return true;
	}

	private final String m_fullName;
	private final long m_id;
	private final Class<?> m_cls;

	public RuntimeClassType(final String fullName, final long id) {
		super(TypeEnum.CLASS, null);
		m_fullName = fullName;
		m_id = id;
		m_cls = doClassOf();
	}

	private Class<?> doClassOf() {
		try {
			return Class.forName(m_fullName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
