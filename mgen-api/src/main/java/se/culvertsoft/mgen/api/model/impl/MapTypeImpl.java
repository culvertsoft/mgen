package se.culvertsoft.mgen.api.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import se.culvertsoft.mgen.api.model.CustomType;
import se.culvertsoft.mgen.api.model.MapType;
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
		m_keyType = new UnlinkedCustomType(writtenKeyType, -1);
		m_valueType = new UnlinkedCustomType(writtenValueType, -1);
	}

	public Type keyType() {
		return m_keyType;
	}

	public Type valueType() {
		return m_valueType;
	}

	public void setKeyType(final Type keyType) {
		m_keyType = keyType;
	}

	public void setValueType(final Type valueType) {
		m_valueType = valueType;
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
	public boolean isLinked() {
		return m_keyType.isLinked() && m_valueType.isLinked();
	}

	@Override
	public Set<CustomType> referencedTypes() {
		final HashSet<CustomType> out = new HashSet<CustomType>();
		out.addAll(m_keyType.referencedTypes());
		out.addAll(m_valueType.referencedTypes());
		return out;
	}

	@Override
	public Class<?> doClassOf() {
		return HashMap.class;
	}

	@Override
	public boolean containsCustomType() {
		return keyType().containsCustomType()
				|| valueType().containsCustomType();
	}

}
