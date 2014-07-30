package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import se.culvertsoft.mgen.api.exceptions.AnalysisException;
import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue;
import se.culvertsoft.mgen.api.model.ListOrArrayType;
import se.culvertsoft.mgen.api.model.Module;

/**
 * Represents a default value for a list or array field/type.
 */
public class ListOrArrayDefaultValueImpl extends DefaultValueImpl implements
		ListOrArrayDefaultValue {

	/**
	 * The type of this default value
	 */
	@Override
	public ListOrArrayType expectedType() {
		return (ListOrArrayType) super.expectedType();
	}

	/**
	 * The list values of this default value
	 */
	public List<DefaultValue> values() {
		return m_values;
	}

	public ListOrArrayDefaultValueImpl(
			final ListOrArrayType typ,
			final String writtenString,
			final Module currentModule) {
		super(typ, writtenString);
		m_values = new ArrayList<DefaultValue>();
		try {
			final Object src = JSONValue.parseWithException(writtenString);
			if (src instanceof JSONArray) {
				for (final Object o : (JSONArray) src) {
					m_values.add(DefaultValueImpl.parse(
							typ.elementType(),
							o.toString(),
							currentModule));
				}
			} else {
				throw new AnalysisException("Failed to parse default value '" + writtenString
						+ "' as a JSON array.");
			}
		} catch (final Exception e) {
			throw new AnalysisException(e);
		}
	}

	private final ArrayList<DefaultValue> m_values;

}
