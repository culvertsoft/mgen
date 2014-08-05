package se.culvertsoft.mgen.api.model;

/**
 * Represents a 16 bit fixed point number type
 */
public class Int16Type extends PrimitiveType {

	private Int16Type() {
		super(TypeEnum.INT16, short.class, "int16");
	}

	public final static Int16Type INSTANCE = new Int16Type();
}
