package se.culvertsoft.mgen.api.model.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.exceptions.MGenException;
import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.EnumEntry;
import se.culvertsoft.mgen.api.model.EnumType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class EnumTypeImpl extends TypeImpl implements EnumType {

	private final String m_name;
	private final String m_fullName;
	private final Module m_module;

	public EnumTypeImpl(final String shortName, final String fullName, final Module module) {
		super(TypeEnum.ENUM);
		m_name = shortName;
		m_fullName = fullName;
		m_module = module;
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

	@Override
	public Set<CustomType> referencedTypes() {
		return Collections.emptySet();
	}

	@Override
	public List<EnumEntry> entries() {
		return Collections.emptyList();
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

}
