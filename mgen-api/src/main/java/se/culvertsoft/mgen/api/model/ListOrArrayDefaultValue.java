package se.culvertsoft.mgen.api.model;

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

	public ListOrArrayDefaultValue(
			final ListOrArrayType typ,
			final String writtenString,
			final Module currentModule) {
		super(typ, writtenString);
		throw new RuntimeException("Not yet implemented!");
	}

}
