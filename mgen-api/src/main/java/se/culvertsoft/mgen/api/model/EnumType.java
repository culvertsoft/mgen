package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an enumeration
 */
public class EnumType extends UserDefinedType {

	/**
	 * The entries of this enumeration.
	 * 
	 * @return The entries of this enumeration.
	 */
	public List<EnumEntry> entries() {
		return m_entries;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return m_name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return m_fullName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return false;
	}

	/**
	 * Sets the entries/values of this enumeration
	 * 
	 * @param entries
	 *            The new entries/values of this enumeration
	 */
	public void setEntries(final List<EnumEntry> entries) {
		m_entries.clear();
		m_entries.addAll(entries);
	}

	/**
	 * Adds a single new entry to this enumeration
	 * 
	 * @param e
	 *            The entry to add
	 */
	public void addEntry(final EnumEntry e) {
		m_entries.add(e);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return null;
	}

	/**
	 * Creates a new enumeration type
	 * 
	 * @param shortName
	 *            The short name of the enumeration
	 * 
	 * @param fullName
	 *            The full name of the enumeration
	 * 
	 * @param module
	 *            The module wherein this enumeration is defined
	 */
	public EnumType(final String shortName, final String fullName, final Module module) {
		super(TypeEnum.ENUM, module);
		m_name = shortName;
		m_fullName = fullName;
		m_entries = new ArrayList<EnumEntry>();
	}

	private final String m_name;
	private final String m_fullName;
	private final List<EnumEntry> m_entries;

	public static final EnumType INSTANCE = new EnumType("0xFFFF", "UNKNOWN", null);

}
