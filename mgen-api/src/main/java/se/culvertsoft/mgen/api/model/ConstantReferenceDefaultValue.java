package se.culvertsoft.mgen.api.model;

/**
 * Represents a reference to a static class constant.
 */
public class ConstantReferenceDefaultValue extends DefaultValue {

	/**
	 * The static constant that this references
	 */
	public Constant constant() {
		return m_constant;
	}

	public ConstantReferenceDefaultValue(
			final Type expectedType,
			final Constant constant,
			final ClassType referencedFrom) {
		super(expectedType, referencedFrom);
		m_constant = constant;
	}

	private final Constant m_constant;

}
