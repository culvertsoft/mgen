package se.culvertsoft.mgen.api.model;

/**
 * Represents a boolean type
 */
public class BoolType extends PrimitiveType {

	private BoolType() {
		super(TypeEnum.BOOL, boolean.class, "bool");
	}

	public final static BoolType INSTANCE = new BoolType();

}
