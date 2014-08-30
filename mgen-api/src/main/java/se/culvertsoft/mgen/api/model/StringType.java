package se.culvertsoft.mgen.api.model;

/**
 * Represents a string type
 */
public class StringType extends Type {

	public final static StringType INSTANCE = new StringType();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return "string";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return "string";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLinked() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return String.class;
	}

	private StringType() {
		super(TypeEnum.STRING);
	}

}
