package se.culvertsoft.mgen.api.model;

/**
 * Represents an unlinked (before the compiler's type linkage stage) value.
 */
public interface UnlinkedDefaultValue extends DefaultValue {

	/**
	 * The actual written default value string in the IDL used 
	 */
	String writtenString();

}
