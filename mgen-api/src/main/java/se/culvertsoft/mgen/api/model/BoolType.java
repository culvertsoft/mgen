package se.culvertsoft.mgen.api.model;

/**
 * Represents a boolean field type
 */
public class BoolType extends PrimitiveType {

	/**
	 * Creates a new boolean type
	 */
	private BoolType() {
		super(TypeEnum.BOOL, boolean.class, "bool");
	}

	public final static BoolType INSTANCE = new BoolType();

}
