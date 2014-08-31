package se.culvertsoft.mgen.api.model;

import java.util.List;

/**
 * Represents a default value for a list or array field/type.
 */
public class ListOrArrayDefaultValue extends DefaultValue {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListOrArrayType expectedType() {
		return (ListOrArrayType) super.expectedType();
	}

	/**
	 * Gets the list values of this default value
	 * 
	 * @return The list values of this default value
	 */
	public List<DefaultValue> values() {
		return m_values;
	}

	/**
	 * Creates a new ListOrArrayDefaultValue
	 * 
	 * @param typ
	 *            The type of this ListOrArrayDefaultValue
	 * 
	 * @param values
	 *            The elements of this ListOrArrayDefaultValue
	 * 
	 * @param referencedFrom
	 *            The class wherein this default value is defined
	 */
	public ListOrArrayDefaultValue(
			final ListOrArrayType typ,
			final List<DefaultValue> values,
			final ClassType referencedFrom) {
		super(typ, referencedFrom);
		m_values = values;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[" + m_values + "]";
	}

	private final List<DefaultValue> m_values;

}
