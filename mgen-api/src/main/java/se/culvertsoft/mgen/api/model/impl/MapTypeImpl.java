package se.culvertsoft.mgen.api.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Module;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public class MapTypeImpl extends TypeImpl implements MapType {

	private Type m_keyType;
	private Type m_valueType;

	public MapTypeImpl(final Type keyType, final Type valueType) {
		super(TypeEnum.MAP);
		m_keyType = keyType;
		m_valueType = valueType;
	}

	public MapTypeImpl(
			final String writtenKeyType,
			final String writtenValueType) {
		super(TypeEnum.MAP);
		m_keyType = new UnknownCustomTypeImpl(writtenKeyType, -1);
		m_valueType = new UnknownCustomTypeImpl(writtenValueType, -1);
	}

	public Type keyType() {
		return m_keyType;
	}

	public Type valueType() {
		return m_valueType;
	}

	public void setKeyType(final Type keyType) {
		m_keyType = keyType;
		resetHashCaches();
	}

	public void setValueType(final Type valueType) {
		m_valueType = valueType;
		resetHashCaches();
	}

	@Override
	public String fullName() {
		return "map[" + m_keyType.fullName() + "," + m_valueType.fullName()
				+ "]";
	}

	@Override
	public String shortName() {
		return fullName();
	}

	@Override
	public boolean isTypeKnown() {
		return m_keyType.isTypeKnown() && m_valueType.isTypeKnown();
	}

	@Override
	public Set<Module> getAllReferencedModulesInclSuper() {
		final HashSet<Module> out = new HashSet<Module>();
		out.addAll(m_keyType.getAllReferencedModulesInclSuper());
		out.addAll(m_valueType.getAllReferencedModulesInclSuper());
		return out;
	}

	@Override
	public Set<CustomType> getAllReferencedTypesInclSuper() {
		final HashSet<CustomType> out = new HashSet<CustomType>();
		out.addAll(m_keyType.getAllReferencedTypesInclSuper());
		out.addAll(m_valueType.getAllReferencedTypesInclSuper());
		return out;
	}

	@Override
	public Class<?> doClassOf() {
		return HashMap.class;
	}

	@Override
	public boolean containsMgenCreatedType() {
		return keyType().containsMgenCreatedType()
				|| valueType().containsMgenCreatedType();
	}

}
