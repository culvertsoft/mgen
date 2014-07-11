package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.EnumEntry;

public class EnumEntryImpl implements EnumEntry {

	private final String m_name;
	private final String m_constant;

	public EnumEntryImpl(final String name, final String constant) {
		m_name = name;
		m_constant = constant;
	}

	@Override
	public String name() {
		return m_name;
	}

	@Override
	public String constant() {
		return m_constant;
	}

}
