package se.culvertsoft.mgen.api.model;

/**
 * Represents an unlinked (before the compiler's type linkage stage) value.
 */
public class UnlinkedDefaultValue extends DefaultValue {

	public UnlinkedDefaultValue(final String writtenString) {
		super(null, writtenString);
	}

}
