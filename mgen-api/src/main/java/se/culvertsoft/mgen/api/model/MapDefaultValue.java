package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a default value for a map field/type.
 */
public interface MapDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	MapType expectedType();

	/**
	 * The map values of this default value
	 */
	Map<DefaultValue, DefaultValue> values();

}
