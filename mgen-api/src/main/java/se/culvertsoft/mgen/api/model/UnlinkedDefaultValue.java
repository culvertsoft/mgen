package se.culvertsoft.mgen.api.model;

/**
 * Represents an unlinked (before the compiler's type linkage stage) value.
 */
public interface UnlinkedDefaultValue extends DefaultValue {

	/**
	 * Used for delayed parsing of the default value. This will be called after
	 * the compiler has linked together types in a project.
	 */
	DefaultValue parse(final Type fieldType, final Module parentModule);

}
