package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.BoolDefaultValue;
import se.culvertsoft.mgen.api.model.BoolType;

public class BoolDefaultValueImpl extends DefaultValueImpl implements BoolDefaultValue {

	/**
	 * Returns the boolean value represented by this default value
	 */
	@Override
	public boolean value() {
		return m_value;
	}

	/**
	 * The type of this enum default value
	 */
	@Override
	public BoolType expectedType() {
		return (BoolType) super.expectedType();
	}

	public BoolDefaultValueImpl(final boolean value) {
		super(BoolType.INSTANCE);
		m_value = value;
	}

	private final boolean m_value;

}
