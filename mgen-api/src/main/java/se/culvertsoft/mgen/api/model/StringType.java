package se.culvertsoft.mgen.api.model;

/**
 * Represents a string type
 */
public class StringType extends Type {

	private StringType() {
		super(TypeEnum.STRING);
	}

	public final static StringType INSTANCE = new StringType();

	@Override
	public String shortName() {
		return "string";
	}

	@Override
	public String fullName() {
		return "string";
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public boolean containsUserDefinedType() {
		return false;
	}

	@Override
	public Class<?> classOf() {
		return String.class;
	}

}
