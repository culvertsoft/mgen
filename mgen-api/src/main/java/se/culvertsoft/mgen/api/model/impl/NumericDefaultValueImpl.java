package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;
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

	public NumericDefaultValueImpl(final PrimitiveType typ, final String writtenString) {
		super(typ, writtenString);
		Number v = null;
		try {
			v = java.lang.Long.decode(writtenString);
		} catch (final NumberFormatException e1) {
			try {
				v = java.lang.Double.parseDouble(writtenString);
			} catch (final NumberFormatException e2) {
				throw new AnalysisException("Failed to parse default value number from "
						+ writtenString, e2);
			}
		}
		m_value = v;
	}

	private final Number m_value;

}
