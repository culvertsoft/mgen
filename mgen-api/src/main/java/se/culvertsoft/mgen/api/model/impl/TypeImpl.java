package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;
import se.culvertsoft.mgen.api.util.Base64;
import se.culvertsoft.mgen.api.util.Hasher;

public abstract class TypeImpl implements Type {

	private final TypeEnum m_enum;
	private Long m_typeId;
	private Short m_typeId16Bit;
	private String m_typeId16BitBase64;
	private Class<?> m_class;

	public TypeImpl(final TypeEnum enm) {
		m_enum = enm;
		m_typeId = null;
		m_typeId16Bit = null;
		m_typeId16BitBase64 = null;
		m_class = null;
	}

	public TypeEnum typeEnum() {
		return m_enum;
	}

	@Override
	public long typeId() {
		if (m_typeId == null)
			m_typeId = Hasher.static_64bit(fullName());
		return m_typeId;
	}

	public short typeId16Bit() {
		if (m_typeId16Bit == null)
			m_typeId16Bit = Hasher.static_16bit(fullName());
		return m_typeId16Bit;
	}

	public String typeId16BitBase64() {
		if (m_typeId16BitBase64 == null)
			m_typeId16BitBase64 = Base64.encode(typeId16Bit());
		return m_typeId16BitBase64;
	}

	public boolean isPrimitive() {
		return false;
	}

	public boolean isSimple() {
		return false;
	}

	public abstract String fullName();

	protected void resetHashCaches() {
		m_typeId = null;
		m_typeId16Bit = null;
		m_typeId16BitBase64 = null;
	}

	@Override
	public int hashCode() {
		return typeId16Bit();
	}

	@Override
	public String toString() {
		return fullName();
	}

	@Override
	public byte binaryTypeTag() {
		return typeEnum().binaryMetadatTag();
	}

	public boolean isMGenCreatedType() {
		return typeEnum() == TypeEnum.UNKNOWN || typeEnum() == TypeEnum.CUSTOM
				|| typeEnum() == TypeEnum.MGEN_BASE;
	}

	@Override
	public final Class<?> classOf() {
		if (m_class == null)
			m_class = doClassOf();
		return m_class;
	}

	public abstract Class<?> doClassOf();

}
