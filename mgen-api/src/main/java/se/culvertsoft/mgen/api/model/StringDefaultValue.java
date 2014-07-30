package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a string field/type.
 */
public interface StringDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	StringType expectedType();

	/**
	 * Returns the string written for this default value
	 */
	String value();

}
