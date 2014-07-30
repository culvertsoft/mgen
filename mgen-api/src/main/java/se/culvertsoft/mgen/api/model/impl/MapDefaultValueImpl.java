package se.culvertsoft.mgen.api.model.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;
import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.MapDefaultValue;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Module;

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

	@SuppressWarnings("unchecked")
	public MapDefaultValueImpl(
			final MapType typ,
			final String writtenString,
			final Module currentModule) {
		super(typ, writtenString);
		m_values = new HashMap<DefaultValue, DefaultValue>();
		try {
			final Object src = JSONValue.parseWithException(writtenString);
			if (src instanceof JSONObject) {
				for (final Map.Entry<String, Object> e : ((Map<String, Object>) src).entrySet()) {
					final DefaultValue key = DefaultValueImpl.parse(
							typ.keyType(),
							e.getKey(),
							currentModule);
					final DefaultValue value = DefaultValueImpl.parse(typ.valueType(), e
							.getValue()
							.toString(), currentModule);
					m_values.put(key, value);
				}
			} else {
				throw new AnalysisException("Failed to parse default value '" + writtenString
						+ "' as a JSON object.");
			}
		} catch (final Exception e) {
			throw new AnalysisException(e);
		}
	}

	private final HashMap<DefaultValue, DefaultValue> m_values;

}
