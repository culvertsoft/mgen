package se.culvertsoft.mgen.api.model;

/**
 * Represents a reference to a static class constant. This type is not yet used
 * in the MGen compiler, but is planned to be used in the future.
 */
public class ConstantReferenceDefaultValue extends DefaultValue {

	/**
	 * Gets the static constant that this default value references
	 * 
	 * @return The static constant that this default value references
	 */
	public Constant constant() {
		return m_constant;
	}

	/**
	 * Creates a new ConstantReferenceDefaultValue - A default value assigned to
	 * a previously specified class constant.
	 * 
	 * @param expectedType
	 *            The expected type of this default value
	 * 
	 * @param constant
	 *            The constant referenced from this default value
	 * 
	 * @param referencedFrom
	 *            The class within which this default value is defined
	 */
	public ConstantReferenceDefaultValue(
			final Type typ,
			final Constant constant,
			final ClassType referencedFrom) {
		super(typ, referencedFrom);
		m_constant = constant;
	}

	private final Constant m_constant;

}
