package se.culvertsoft.mgen.api.model;

/**
 * Represents a 32 bit fixed point number type
 */
public class Int32Type extends FixedPointType {

	private Int32Type() {
		super(TypeEnum.INT32, int.class, "int32");
	}

	public final static Int32Type INSTANCE = new Int32Type();
}
