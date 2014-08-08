package se.culvertsoft.mgen.api.model;

/**
 * Represents a 64 bit fixed point number type
 */
public class Int64Type extends FixedPointType {

	private Int64Type() {
		super(TypeEnum.INT64, long.class, "int64");
	}

	public final static Int64Type INSTANCE = new Int64Type();
}