package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value specified in the IDL for a field.
 */
public abstract class DefaultValue {

	/**
	 * The type of this default value
	 */
	public Type typ() {
		return m_type;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	public String writtenString() {
		return m_writtenString;
	}

	/**
	 * The actual written default value string in the IDL
	 */
	public boolean isLinked() {
		return m_type != null;
	}

	protected DefaultValue(final Type typ, final String writtenString) {
		m_type = typ;
		m_writtenString = writtenString;
	}

	private Type m_type;
	private String m_writtenString;

	/**
	 * Intended to be used by the compiler during the type linkage phase to
	 * parse default value strings.
	 */
	public static DefaultValue parse(final String writtenString, final Module type) {
		throw new RuntimeException("Not yet implemented!");
	}

}
