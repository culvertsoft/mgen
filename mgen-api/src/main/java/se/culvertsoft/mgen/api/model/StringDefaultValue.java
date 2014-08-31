package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a string field/type.
 */
public class StringDefaultValue extends DefaultValue {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StringType expectedType() {
		return (StringType) super.expectedType();
	}

	/**
	 * Returns the string written for this default value
	 * 
	 * @return the default value of this type
	 */
	public String value() {
		return m_value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return '"' + m_value + '"';
	}

	public StringDefaultValue(
			final String value,
			final ClassType referencedFrom) {
		super(StringType.INSTANCE, referencedFrom);
		m_value = value;
	}

	private final String m_value;

}
