package se.culvertsoft.mgen.api.model;

/**
 * Base class for all unlinked default values. UnlinkedDefaultValue instances
 * are created by parsers during the compilers first pass. They are resolved to
 * normal DefaultValue instances during the compiler's second pass (the linkage
 * phase).
 * 
 * Parsers are not required to fully resolve default values from the IDL during
 * the compiler's first pass, but may instead create shallow default values
 * (UnlinkedDefaultValue). That is - they can defer the complete resolution of
 * default values to the compiler's second pass (the linking phase), when more
 * information may be available on other types present in the current MGen
 * Project.
 * 
 * UnlinkedDefaultValue represents a not yet fully resolved default value.
 * UnlinkedDefaultValue.parse(..) will be called for all UnlinkedDefaultValue
 * instances by the compiler during its second pass (the linking phase) to fully
 * resolve these default values.
 */
public abstract class UnlinkedDefaultValue extends DefaultValue {

	/**
	 * UnlinkedDefaultValue subclasses may resolve their values in different
	 * ways, and the abstract parse(..) method provides them an interface to do
	 * this
	 * 
	 * @param expectedType
	 *            The type that this default value is expected to have. Default
	 *            values are associated with a class field, and the expectedType
	 *            is the type of that field.
	 * 
	 * @param lookup
	 *            A convenience database for finding types and constants by name
	 *            in the current MGen project.
	 * 
	 * @return The fully resolved DefaultValue
	 */
	public abstract DefaultValue parse(final Type expectedType, final ItemLookup lookup);

	/**
	 * Creates a new UnlinkedDefault
	 * 
	 * @param referencedFrom
	 *            The class from from where this UnlinkedDefaultValue is
	 *            referenced (e.g. field x of class Y).
	 */
	public UnlinkedDefaultValue(final ClassType referencedFrom) {
		super(null, referencedFrom);
	}

}
