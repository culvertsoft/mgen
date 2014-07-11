package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.List;

import se.culvertsoft.mgen.api.exceptions.MGenException;
import se.culvertsoft.mgen.api.model.EnumEntry;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class EnumTypeImpl extends SimpleTypeImpl implements EnumType {

	private final String m_name;
	private final String m_fullName;
	private final Module m_module;
	private final List<EnumEntryImpl> m_entries;

	public EnumTypeImpl(final String shortName, final String fullName, final Module module) {
		super(TypeEnum.ENUM);
		m_name = shortName;
		m_fullName = fullName;
		m_module = module;
		m_entries = new ArrayList<EnumEntryImpl>();
	}

	public EnumTypeImpl(final String shortName, final String fullName) {
		this(shortName, fullName, null);
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
	public boolean isLinked() {
		return m_module != null;
	}

	@Override
	public boolean containsCustomType() {
		return false;
	}

	public void setEntries(final List<EnumEntryImpl> entries) {
		m_entries.clear();
		m_entries.addAll(entries);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<EnumEntry> entries() {
		return (List) m_entries;
	}

	@Override
	public Module module() {
		if (isLinked())
			return m_module;
		throw new MGenException("Type details unknown: Cannot call module()");
	}

	@Override
	public Class<?> doClassOf() {
		try {
			return Class.forName(m_fullName);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public Enum<?> get(final String entryStringName) {
		throw new MGenException("Type details unknown: Cannot call get(String)");
	}

	public Enum<?> get(final int entryIntvalue) {
		throw new MGenException("Type details unknown: Cannot call get(int)");
	}

}
