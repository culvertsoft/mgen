package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a numeric field/type.
 */
public interface NumericDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	PrimitiveType expectedType();

	/**
	 * Gets the stored value as a double
	 */
	double floatingPtValue();

	/**
	 * Gets the stored value as a long
	 */
	long fixedPtValue();

}
