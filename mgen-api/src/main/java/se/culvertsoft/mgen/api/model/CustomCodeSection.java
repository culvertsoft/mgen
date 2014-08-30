package se.culvertsoft.mgen.api.model;

/**
 * Represents a custom code section in generated code. Custom code sections are
 * sections in generated code intended to be modified by the developer after
 * code generation. If re-generating the same or an updated version of the
 * model, the compiler should ensure that custom code sections are preserved and
 * that the custom code within is copied to the newly generated file.
 */
public class CustomCodeSection {

	/**
	 * Gets the key that indicates the start of the custom code section.
	 * 
	 * @return the key that indicates the start of the custom code section.
	 */
	public String getBeginKey() {
		return m_beginKey;
	}

	/**
	 * Gets the key that indicates the end of the custom code section.
	 * 
	 * @return the key that indicates the end of the custom code section.
	 */
	public String getEndKey() {
		return m_endKey;
	}

	/**
	 * Creates a new custom code section
	 * 
	 * @param beginKey
	 *            The string which indicates the start of the section
	 * 
	 * @param endKey
	 *            The string which indicates the end of the section
	 */
	public CustomCodeSection(final String beginKey, final String endKey) {
		m_beginKey = beginKey;
		m_endKey = endKey;
	}

	/**
	 * Returns getBeginKey() + getEndKey()
	 * 
	 * @return getBeginKey() + getEndKey()
	 */
	@Override
	public String toString() {
		return m_beginKey + m_endKey;
	}

	private final String m_beginKey;
	private final String m_endKey;

}
