package se.culvertsoft.mgen.api.model;

import java.util.Map;

/**
 * Represents a default value for a map field/type.
 */
public class MapDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	public MapType expectedType() {
		return (MapType) super.expectedType();
	}

	/**
	 * The map values of this default value
	 */
	public Map<DefaultValue, DefaultValue> values() {
		return m_values;
	}

	public MapDefaultValue(
			final MapType typ,
			final Map<DefaultValue, DefaultValue> values) {
		super(typ);
		m_values = values;
	}

	private final Map<DefaultValue, DefaultValue> m_values;

}
