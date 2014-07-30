package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a bool field/type.
 */
public interface BoolDefaultValue extends DefaultValue {

	/**
	 * Returns the boolean value represented by this default value
	 */
	boolean value();

	/**
	 * The type of this enum default value
	 */
	@Override
	BoolType expectedType();

}
