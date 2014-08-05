package se.culvertsoft.mgen.api.model;

/**
 * Represents an entry in an enumeration
 * 
 * @author GiGurra
 * 
 */
public class EnumEntry {

	/**
	 * The written name of the entry
	 * 
	 * @return The written name of the entry
	 */
	public String name() {
		return m_name;
	}

	/**
	 * The value specified for the enum, or null otherwise
	 * 
	 * @return The value specified for the enum, or null otherwise
	 */
	public String constant() {
		return m_constant;
	}

	public EnumEntry(final String name, final String constant) {
		m_name = name;
		m_constant = constant;
	}

	private final String m_name;
	private final String m_constant;

}
