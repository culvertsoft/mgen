package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a string field/type.
 */
public class StringDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	public StringType typ() {
		return (StringType) super.typ();
	}

	/**
	 * Returns the string written for this default value
	 */
	public String value() {
		return writtenString();
	}

	public StringDefaultValue(final StringType typ, final String writtenString) {
		super(typ, writtenString);
	}

}
