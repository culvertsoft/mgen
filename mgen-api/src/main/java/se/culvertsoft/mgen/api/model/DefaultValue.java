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
	 * The actual written default value string in the IDL
	 */
	String writtenString();

	/**
	 * The actual written default value string in the IDL
	 */
	boolean isLinked();

}
