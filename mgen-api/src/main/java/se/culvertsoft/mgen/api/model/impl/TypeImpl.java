package se.culvertsoft.mgen.api.model.impl;

import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.api.model.TypeEnum;
import se.culvertsoft.mgen.api.util.Base64;
import se.culvertsoft.mgen.api.util.Hasher;

public abstract class TypeImpl implements Type {

	private final TypeEnum m_enum;
	private Integer m_32bitHash;
	private Short m_16bitHash;
	private String m_32BitHashBase64;
	private String m_16BitHashBase64;
	private Class<?> m_class;

	public TypeImpl(final TypeEnum enm) {
		m_enum = enm;
		m_32bitHash = null;
		m_16bitHash = null;
		m_32BitHashBase64 = null;
		m_16BitHashBase64 = null;
		m_class = null;
	}

	public TypeEnum typeEnum() {
		return m_enum;
	}

	public int typeHash32bit() {
		if (m_32bitHash == null)
			m_32bitHash = Hasher.static_32bit(fullName());
		return m_32bitHash;
	}

	public short typeHash16bit() {
		if (m_16bitHash == null)
			m_16bitHash = Hasher.static_16bit(fullName());
		return m_16bitHash;
	}

	public String typeHash32bitBase64String() {
		if (m_32BitHashBase64 == null)
			m_32BitHashBase64 = Base64.encode(typeHash32bit());
		return m_32BitHashBase64;
	}

	public String typeHash16bitBase64String() {
		if (m_16BitHashBase64 == null)
			m_16BitHashBase64 = Base64.encode(typeHash16bit());
		return m_16BitHashBase64;
	}

	public boolean isPrimitive() {
		return false;
	}

	public boolean isSimple() {
		return false;
	}

	public abstract String fullName();

	protected void resetHashCaches() {
		m_32bitHash = null;
		m_16bitHash = null;
	}

	@Override
	public int hashCode() {
		return typeHash32bit();
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
