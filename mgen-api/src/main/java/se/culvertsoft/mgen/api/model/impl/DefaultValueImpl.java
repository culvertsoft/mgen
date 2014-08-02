package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.Type;

public class DefaultValueImpl implements DefaultValue {

	/**
	 * The expected type of this default value
	 */
	@Override
	public Type expectedType() {
		return m_expectedType;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	@Override
	public boolean isLinked() {
		return m_expectedType != null;
	}

	protected DefaultValueImpl(final Type typ) {
		m_expectedType = typ;
	}

	private Type m_expectedType;


}
