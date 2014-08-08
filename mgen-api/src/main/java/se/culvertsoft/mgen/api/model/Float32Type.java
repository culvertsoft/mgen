package se.culvertsoft.mgen.api.model;

/**
 * Represents a 32 bit floating point number type
 */
public class Float32Type extends FloatingPointType {

	private Float32Type() {
		super(TypeEnum.FLOAT32, float.class, "float32");
	}

	public final static Float32Type INSTANCE = new Float32Type();
}
