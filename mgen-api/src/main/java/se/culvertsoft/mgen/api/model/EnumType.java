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

	@Override
	public String shortName() {
		return m_name;
	}

	@Override
	public String fullName() {
		return m_fullName;
	}

	@Override
	public boolean containsUserDefinedType() {
		return false;
	}

	public void setEntries(final List<EnumEntry> entries) {
		m_entries.clear();
		m_entries.addAll(entries);
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public Class<?> classOf() {
		return null;
	}

	private final String m_name;
	private final String m_fullName;
	private final List<EnumEntry> m_entries;

	public EnumType(
			final String shortName,
			final String fullName,
			final Module module) {
		super(TypeEnum.ENUM, module);
		m_name = shortName;
		m_fullName = fullName;
		m_entries = new ArrayList<EnumEntry>();
	}

	public static final EnumType INSTANCE = new EnumType(
			"0xFFFF",
			"UNKNOWN",
			null);

}
