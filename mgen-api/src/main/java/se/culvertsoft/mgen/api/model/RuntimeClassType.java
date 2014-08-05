package se.culvertsoft.mgen.api.model;

/**
 * This type is intended to be used in generated source code to specify field
 * types, primarily for serializers/deserializers to use.
 */
public class RuntimeClassType extends UserDefinedType {

	/**
	 * 64 bit id hashed from the qualified type name of this class
	 */
	public long typeId() {
		return m_id;
	}

	public boolean isLinked() {
		return true;
	}

	@Override
	public String fullName() {
		return m_fullName;
	}

	@Override
	public String shortName() {
		return fullName();
	}

	@Override
	public Class<?> classOf() {
		return m_cls;
	}

	@Override
	public String toString() {
		return m_fullName;
	}

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
