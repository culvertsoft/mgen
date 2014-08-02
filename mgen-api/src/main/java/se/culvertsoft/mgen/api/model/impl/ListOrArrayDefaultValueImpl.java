package se.culvertsoft.mgen.api.model.impl;

import java.util.ArrayList;
import java.util.List;

import se.culvertsoft.mgen.api.model.DefaultValue;
import se.culvertsoft.mgen.api.model.ListOrArrayDefaultValue;
import se.culvertsoft.mgen.api.model.ListOrArrayType;

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
			final ArrayList<DefaultValue> values) {
		super(typ);
		m_values = values;
	}

	private final ArrayList<DefaultValue> m_values;

}
