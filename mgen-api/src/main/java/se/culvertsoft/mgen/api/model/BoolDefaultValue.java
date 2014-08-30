package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a boolean field.
 */
public class BoolDefaultValue extends DefaultValue {

	/**
	 * Returns the boolean value represented by this default value
	 * 
	 * @return The boolean value represented by this default value
	 */
	public boolean value() {
		return m_value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoolType expectedType() {
		return (BoolType) super.expectedType();
	}

	/**
	 * Creates a new boolean default value
	 * 
	 * @param value
	 *            The value of this default value
	 * 
	 * @param referencedFrom
	 *            From where this default value is referenced
	 */
	public BoolDefaultValue(final boolean value, final ClassType referencedFrom) {
		super(BoolType.INSTANCE, referencedFrom);
		m_value = value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.valueOf(m_value);
	}

	private final boolean m_value;

}
