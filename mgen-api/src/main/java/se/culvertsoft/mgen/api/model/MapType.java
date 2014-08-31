package se.culvertsoft.mgen.api.model;

import java.util.HashMap;

/**
 * Represents a map type in the MGen data model, i.e. a HashMap[K,V] for Java,
 * std::map[K,V] for C++ and plain object ({}) for JavaScript.
 */
public class MapType extends Type {

	/**
	 * Gets the key type of this map
	 * 
	 * @return The key type of this map
	 */
	public Type keyType() {
		return m_keyType;
	}

	/**
	 * Gets the value type of this map
	 * 
	 * @return The value type of this map
	 */
	public Type valueType() {
		return m_valueType;
	}

	/**
	 * Sets the key type of this map
	 * 
	 * @param keyType
	 *            The new key type of this map
	 */
	public void setKeyType(final Type keyType) {
		m_keyType = keyType;
	}

	/**
	 * Sets the value type of this map
	 * 
	 * @param valueType
	 *            The value type of this map
	 */
	public void setValueType(final Type valueType) {
		m_valueType = valueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return "map[" + m_keyType.fullName() + "," + m_valueType.fullName()
				+ "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return fullName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return m_keyType.isLinked() && m_valueType.isLinked();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return HashMap.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return keyType().containsUserDefinedType()
				|| valueType().containsUserDefinedType();
	}

	/**
	 * Creates a new MapType
	 * 
	 * @param keyType
	 *            The key type of the map
	 * 
	 * @param valueType
	 *            The value type of the map
	 */
	public MapType(final Type keyType, final Type valueType) {
		super(TypeEnum.MAP);
		m_keyType = keyType;
		m_valueType = valueType;
	}

	private Type m_keyType;
	private Type m_valueType;

}
