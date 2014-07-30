package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public interface ObjectDefaultValue extends DefaultValue {

	/**
	 * The expected type of this default value
	 */
	@Override
	CustomType expectedType();

	/**
	 * The actual type of this default value, which is either the same as the
	 * expected type or a subtype of it
	 */
	CustomType actualType();

	/**
	 * Returns if the type of this default value is defined in the same module
	 * as where it's now referenced.
	 */
	boolean isCurrentModule();

	/**
	 * Returns if the default value is of a subtype
	 */
	boolean isDefaultTypeOverriden();

	/**
	 * The overridden field default values of this default value
	 */
	Map<Field, DefaultValue> overriddenDefaultValues();

}
