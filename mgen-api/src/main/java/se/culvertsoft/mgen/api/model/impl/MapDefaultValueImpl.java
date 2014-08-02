package se.culvertsoft.mgen.api.model.impl;

import java.util.Map;

import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.MapDefaultValue;
import se.culvertsoft.mgen.api.model.MapType;

/**
 * Represents a default value for a map field/type.
 */
public class MapDefaultValueImpl extends DefaultValueImpl implements MapDefaultValue {

	@Override
	public MapType expectedType() {
		return (MapType) super.expectedType();
	}

	@Override
	public Map<DefaultValue, DefaultValue> values() {
		return m_values;
	}

	public MapDefaultValueImpl(final MapType typ, final Map<DefaultValue, DefaultValue> values) {
		super(typ);
		m_values = values;
	}

	private final Map<DefaultValue, DefaultValue> m_values;

}
