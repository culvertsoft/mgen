package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a numeric field/type.
 */
public class NumericDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	public PrimitiveType expectedType() {
		return (PrimitiveType) super.expectedType();
	}

	/**
	 * Gets the stored value as a double
	 */
	public double floatingPtValue() {
		return m_value.doubleValue();
	}

	/**
	 * Gets the stored value as a long
	 */
	public long fixedPtValue() {
		return m_value.longValue();
	}

	@Override
	public String toString() {
		return m_value.toString();
	}

	public NumericDefaultValue(
			final PrimitiveType expectedType,
			final Number value,
			final ClassType referencedFrom) {
		super(expectedType, referencedFrom);
		m_value = value;
	}

	private final Number m_value;
}
