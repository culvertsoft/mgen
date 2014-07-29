package se.culvertsoft.mgen.api.model;

/**
 * Represents a default value for an object/CustomType field/type.
 */
public class ObjectDefaultValue extends DefaultValue {

	/**
	 * The type of this default value
	 */
	public CustomType typ() {
		return (CustomType) super.typ();
	}
	
	public ObjectDefaultValue(final CustomType typ, final String writtenString) {
		super(typ, writtenString);
	}

}
