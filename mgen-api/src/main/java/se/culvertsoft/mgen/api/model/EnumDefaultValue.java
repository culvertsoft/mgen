package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for an enum field/type.
 */
public interface EnumDefaultValue extends DefaultValue {

	/**
	 * Returns the enum value/entry represented by this default value object
	 */
	EnumEntry value();

	/**
	 * The type of this enum default value
	 */
	@Override
	EnumType expectedType();

	/**
	 * Returns if the type of this default value is defined in the same module
	 * as where it's now referenced.
	 */
	boolean isCurrentModule();

}
