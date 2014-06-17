package se.culvertsoft.mgen.javapack.serialization;

public class SerializationSettings {

	public static final SerializationSettings DEFAULT = new SerializationSettings(
			TypeIdType.HASH_16_BIT,
			FieldIdType.HASH_16_BIT);

	public enum TypeIdType {
		HASH_16_BIT, // DEFAULT
		CLASS_NAME_QUALIFIED,
	}

	public enum FieldIdType {
		HASH_16_BIT, // DEFAULT
		FIELD_NAME
	}

	public TypeIdType typeIdType() {
		return m_typeIdType;
	}

	public FieldIdType fieldIdType() {
		return m_fieldIdType;
	}

	public SerializationSettings(
			final TypeIdType typeIdType,
			final FieldIdType fieldIdType) {
		m_typeIdType = typeIdType;
		m_fieldIdType = fieldIdType;
	}

	@Override
	public String toString() {
		return "MGenSerializationSettings [m_typeIdType=" + m_typeIdType
				+ ", m_fieldIdType=" + m_fieldIdType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((m_fieldIdType == null) ? 0 : m_fieldIdType.hashCode());
		result = prime * result
				+ ((m_typeIdType == null) ? 0 : m_typeIdType.hashCode());
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
		SerializationSettings other = (SerializationSettings) obj;
		if (m_fieldIdType != other.m_fieldIdType)
			return false;
		if (m_typeIdType != other.m_typeIdType)
			return false;
		return true;
	}

	private final TypeIdType m_typeIdType;
	private final FieldIdType m_fieldIdType;

}
