package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;

public abstract class TypeImpl implements Type {

	private final TypeEnum m_enum;
	private Class<?> m_class;

	public TypeImpl(final TypeEnum enm) {
		m_enum = enm;
		m_class = null;
	}

	public TypeEnum typeEnum() {
		return m_enum;
	}

	@Override
	public String toString() {
		return fullName();
	}

	@Override
	public byte typeTag() {
		return typeEnum().binaryMetadatTag();
	}

	@Override
	public final Class<?> classOf() {
		if (m_class == null)
			m_class = doClassOf();
		return m_class;
	}

	public abstract Class<?> doClassOf();

}
