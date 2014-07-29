package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public class ObjectDefaultValue extends DefaultValue {

	/**
	 * The expected type of this default value
	 */
	@Override
	public CustomType expectedType() {
		return (CustomType) super.expectedType();
	}

	/**
	 * The actual type of this default value, which is either the same as the
	 * expected type or a subtype of it
	 */
	public CustomType actualType() {
		return m_actualType;
	}

	public ObjectDefaultValue(final CustomType expectedType, final String writtenString) {
		super(expectedType, writtenString);
	}

	private CustomType m_actualType;

}
