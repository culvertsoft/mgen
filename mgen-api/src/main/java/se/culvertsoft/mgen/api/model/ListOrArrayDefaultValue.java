package se.culvertsoft.mgen.api.model;

import java.util.List;

/**
 * Represents a default value for a list or array field/type.
 */
public class ListOrArrayDefaultValue extends DefaultValue {

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

	/**
	 * The list values of this default value
	 */
	public ListOrArrayDefaultValue(final ListOrArrayType typ, final List<DefaultValue> values) {
		super(typ);
		m_values = values;
	}

	@Override
	public String toString() {
		return "[" + m_values + "]";
	}

	private final List<DefaultValue> m_values;

}
