package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.StringDefaultValue;
import se.culvertsoft.mgen.api.model.StringType;

/**
 * Represents a default value for a string field/type.
 */
public class StringDefaultValueImpl extends DefaultValueImpl implements StringDefaultValue {

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
	@Override
	public String value() {
		return writtenString();
	}

	public StringDefaultValueImpl(final StringType typ, final String writtenString) {
		super(typ, writtenString);
	}

}
