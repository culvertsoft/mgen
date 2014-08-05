package se.culvertsoft.mgen.api.model;

import java.util.HashMap;

/**
 * Interface for all map types (e.g. map[A,B])
 */
public class MapType extends Type {

	/**
	 * The type of the keys of this map
	 */
	public Type keyType() {
		return m_keyType;
	}

	/**
	 * The type of the values of this map
	 */
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
	public Class<?> classOf() {
		return HashMap.class;
	}

	@Override
	public boolean containsUserDefinedType() {
		return keyType().containsUserDefinedType()
				|| valueType().containsUserDefinedType();
	}

	private Type m_keyType;
	private Type m_valueType;

	public MapType(final Type keyType, final Type valueType) {
		super(TypeEnum.MAP);
		m_keyType = keyType;
		m_valueType = valueType;
	}

}
