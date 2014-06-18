package se.culvertsoft.mgen.api.model.impl;

import java.util.Collections;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.TypeEnum;
import se.culvertsoft.mgen.api.model.UnknownCustomType;

public class UnknownCustomTypeImpl extends TypeImpl implements
		UnknownCustomType {

	private final String m_writtenType;
	private final long m_typeId;

	public UnknownCustomTypeImpl(final String writtenType, final long typeId) {
		super(TypeEnum.UNKNOWN);
		m_writtenType = writtenType;
		m_typeId = typeId;
	}

	@Override
	public long typeId() {
		return m_typeId;
	}

	@Override
	public String fullName() {
		return m_writtenType;
	}

	@Override
	public String shortName() {
		return fullName();
	}

	public String writtenType() {
		return m_writtenType;
	}

	@Override
	public String toString() {
		return fullName();
	}

	@Override
	public boolean isTypeKnown() {
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Module> getAllReferencedModulesInclSuper() {
		return Collections.EMPTY_SET;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<CustomType> getAllReferencedTypesInclSuper() {
		return Collections.EMPTY_SET;
	}

	@Override
	public Class<?> doClassOf() {
		try {
			return Class.forName(fullName());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean containsMgenCreatedType() {
		return true;
	}

}
