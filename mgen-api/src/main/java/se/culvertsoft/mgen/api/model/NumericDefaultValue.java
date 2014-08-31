package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a numeric field/type.
 */
public class NumericDefaultValue extends DefaultValue {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrimitiveType expectedType() {
		return (PrimitiveType) super.expectedType();
	}

	/**
	 * Gets the stored value as a float64
	 * 
	 * @return The stored value as a float64
	 */
	public double floatingPtValue() {
		return m_value.doubleValue();
	}

	/**
	 * Gets the stored value as an int64
	 * 
	 * @return The stored value as an int64
	 */
	public long fixedPtValue() {
		return m_value.longValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return m_value.toString();
	}

	/**
	 * Creates a new NumericDefaultValue
	 * 
	 * @param expectedType
	 *            The expected type of the field with this default value
	 * 
	 * @param value
	 *            The actual value of this DefaultValue
	 * 
	 * @param referencedFrom
	 *            The class wherein this default value is defined
	 */
	public NumericDefaultValue(
			final PrimitiveType expectedType,
			final Number value,
			final ClassType referencedFrom) {
		super(expectedType, referencedFrom);
		m_value = value;
	}

	private final Number m_value;
}
