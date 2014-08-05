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
	 * If the compiler has yet linked this default values. Linking means the
	 * compiler running its second pass where custom class and enum types are
	 * linked to fields (going from being just names/strings).
	 */
	public boolean isLinked() {
		return m_expectedType != null;
	}

	protected DefaultValue(final Type typ) {
		m_expectedType = typ;
	}

	private Type m_expectedType;

}
