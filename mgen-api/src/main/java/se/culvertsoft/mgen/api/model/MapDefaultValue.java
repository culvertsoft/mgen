package se.culvertsoft.mgen.api.model;

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

	public MapDefaultValue(final MapType typ, final String writtenString, final Module module) {
		super(typ, writtenString);
	}

}
