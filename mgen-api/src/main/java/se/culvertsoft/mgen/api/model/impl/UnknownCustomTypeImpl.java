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
	private final int m_localTypeId;

	public UnknownCustomTypeImpl(final String writtenType, final int localTypeId) {
		super(TypeEnum.UNKNOWN);
		m_writtenType = writtenType;
		m_localTypeId = localTypeId;
	}

	@Override
	public int localTypeId() {
		return m_localTypeId;
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + m_localTypeId;
		result = prime * result
				+ ((m_writtenType == null) ? 0 : m_writtenType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnknownCustomTypeImpl other = (UnknownCustomTypeImpl) obj;
		if (m_localTypeId != other.m_localTypeId)
			return false;
		if (m_writtenType == null) {
			if (other.m_writtenType != null)
				return false;
		} else if (!m_writtenType.equals(other.m_writtenType))
			return false;
		return true;
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
