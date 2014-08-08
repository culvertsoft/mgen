package se.culvertsoft.mgen.api.model;

/**
 * Represents an 8 bit fixed point number type (= a byte)
 */
public class Int8Type extends FixedPointType {

	private Int8Type() {
		super(TypeEnum.INT8, byte.class, "int8");
	}

	public final static Int8Type INSTANCE = new Int8Type();
}