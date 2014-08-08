package se.culvertsoft.mgen.api.model;

/**
 * Represents a 64 bit floating point number type
 */
public class Float64Type extends FloatingPointType {

	private Float64Type() {
		super(TypeEnum.FLOAT64, double.class, "float64");
	}

	public final static Float64Type INSTANCE = new Float64Type();

}
