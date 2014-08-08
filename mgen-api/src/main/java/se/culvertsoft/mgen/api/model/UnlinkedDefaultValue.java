package se.culvertsoft.mgen.api.model;

/**
 * Represents an unlinked (before the compiler's type linkage stage) value.
 */
public abstract class UnlinkedDefaultValue extends DefaultValue {

	/**
	 * Used for delayed parsing of the default value. This will be called after
	 * the compiler has linked together types in a project.
	 */
	public abstract DefaultValue parse(
			final Type fieldType,
			final ClassType referencedFrom,
			final ItemLookup lookup);

	public UnlinkedDefaultValue() {
		super(null);
	}

}
