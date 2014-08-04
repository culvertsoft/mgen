package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value specified in the IDL for a field.
 */
public interface DefaultValue {

	/**
	 * The expected type of this default value
	 */
	Type expectedType();

	/**
	 * If the compiler has yet linked this default values. Linking means the
	 * compiler running its second pass where custom class and enum types are
	 * linked to fields (going from being just names/strings).
	 */
	boolean isLinked();

}
