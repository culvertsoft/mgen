package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for a map field/type.
 */
public class MapDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	public MapType typ() {
		return (MapType) super.typ();
	}

	public MapDefaultValue(final MapType typ, final String writtenString, final Module module) {
		super(typ, writtenString);
	}

}
