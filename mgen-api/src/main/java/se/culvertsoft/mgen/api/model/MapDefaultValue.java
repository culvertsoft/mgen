package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a default value for a map field/type.
 */
public class MapDefaultValue extends DefaultValue {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapType expectedType() {
		return (MapType) super.expectedType();
	}

	/**
	 * Gets the map values of this default value
	 * 
	 * @return The map values of this default value
	 */
	public Map<DefaultValue, DefaultValue> values() {
		return m_values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "{" + m_values + "}";
	}

	/**
	 * Creates a new MapDefaultValue
	 * 
	 * @param typ
	 *            The type meta data about the MapDefaultValue
	 * 
	 * @param values
	 *            The actual values of this MapDefaultValue
	 * 
	 * @param referencedFrom
	 *            The class wherein this MapDefaultValue is defined
	 */
	public MapDefaultValue(
			final MapType typ,
			final Map<DefaultValue, DefaultValue> values,
			final ClassType referencedFrom) {
		super(typ, referencedFrom);
		m_values = values;
	}

	private final Map<DefaultValue, DefaultValue> m_values;

}
