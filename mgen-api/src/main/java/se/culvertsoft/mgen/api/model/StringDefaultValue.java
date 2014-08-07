package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a string field/type.
 */
public class StringDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	public StringType expectedType() {
		return (StringType) super.expectedType();
	}

	/**
	 * Returns the string written for this default value
	 */
	public String value() {
		return m_value;
	}

	@Override
	public String toString() {
		return '"' + m_value + '"';
	}

	public StringDefaultValue(final String value) {
		super(StringType.INSTANCE);
		m_value = value;
	}

	private final String m_value;

}
