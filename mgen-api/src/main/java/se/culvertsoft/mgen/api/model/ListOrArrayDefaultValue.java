package se.culvertsoft.mgen.api.model;

import java.util.List;

/**
 * Represents a default value for a list or array field/type.
 */
public interface ListOrArrayDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	ListOrArrayType expectedType();

	/**
	 * The list values of this default value
	 */
	List<DefaultValue> values();

}
