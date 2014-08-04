package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.NumericDefaultValue;
import se.culvertsoft.mgen.api.model.PrimitiveType;

/**
 * Represents a default value for a numeric field/type.
 */
public class NumericDefaultValueImpl extends DefaultValueImpl implements NumericDefaultValue {

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
	@Override
	public double floatingPtValue() {
		return m_value.doubleValue();
	}

	/**
	 * Gets the stored value as a long
	 */
	@Override
	public long fixedPtValue() {
		return m_value.longValue();
	}

	public NumericDefaultValueImpl(final PrimitiveType expectedType, final Number value) {
		super(expectedType);
		m_value = value;
	}

	private final Number m_value;

}
