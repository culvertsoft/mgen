package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value specified in the IDL for a field.
 */
public class DefaultValue {

	/**
	 * Gets the expected type of this default value
	 * 
	 * @return The expected type of this default value
	 */
	public Type expectedType() {
		return m_expectedType;
	}

	/**
	 * Gets the class from which this default value is referenced
	 * 
	 * @return The class from which this default value is referenced
	 */
	public ClassType referencedFrom() {
		return m_referencedFrom;
	}

	/**
	 * If the compiler has yet linked this default values. Linking means the
	 * compiler running its second pass where custom class and enum types are
	 * linked to fields (going from being just names/strings).
	 * 
	 * @return If this default value is yet linked
	 */
	public boolean isLinked() {
		return m_expectedType != null;
	}

	/**
	 * Creates a new default value
	 * 
	 * @param expectedType
	 *            The expected type of this default value
	 * 
	 * @param referencedFrom
	 *            The class from where this default value is referenced
	 */
	protected DefaultValue(final Type expectedType, final ClassType referencedFrom) {
		m_expectedType = expectedType;
		m_referencedFrom = referencedFrom;
	}

	private final Type m_expectedType;
	private final ClassType m_referencedFrom;

}
