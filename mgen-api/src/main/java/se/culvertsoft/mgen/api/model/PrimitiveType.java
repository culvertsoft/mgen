package se.culvertsoft.mgen.api.model;

/**
 * Base class for all 'primitive' types (numeric types and booleans)
 */
public abstract class PrimitiveType extends Type {

	@Override
	public String fullName() {
		return m_name;
	}

	@Override
	public String shortName() {
		return m_name;
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public boolean containsUserDefinedType() {
		return false;
	}

	@Override
	public Class<?> classOf() {
		return m_cls;
	}

	private final Class<?> m_cls;
	private final String m_name;

	protected PrimitiveType(
			final TypeEnum enm,
			final Class<?> cls,
			final String name) {
		super(enm);
		m_cls = cls;
		m_name = name;
	}

}
