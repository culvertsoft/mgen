package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value specified in the IDL for a field.
 */
public class DefaultValue {

	/**
	 * The expected type of this default value
	 */
	public Type expectedType() {
		return m_expectedType;
	}

	/**
	 * The class from which this default value is referenced
	 */
	public ClassType referencedFrom() {
		return m_referencedFrom;
	}

	/**
	 * If the compiler has yet linked this default values. Linking means the
	 * compiler running its second pass where custom class and enum types are
	 * linked to fields (going from being just names/strings).
	 */
	public boolean isLinked() {
		return m_expectedType != null;
	}

	protected DefaultValue(final Type typ, final ClassType referencedFrom) {
		m_expectedType = typ;
		m_referencedFrom = referencedFrom;
	}

	private final Type m_expectedType;
	private final ClassType m_referencedFrom;

}
