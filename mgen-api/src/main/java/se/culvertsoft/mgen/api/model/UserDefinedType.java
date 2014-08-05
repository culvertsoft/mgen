package se.culvertsoft.mgen.api.model;

public abstract class UserDefinedType extends Type {

	/**
	 * The parent Module that this type is defined within. Returns null if
	 * called from outside the compiler.
	 */
	public Module module() {
		return m_module;
	}

	protected UserDefinedType(final TypeEnum enm, final Module module) {
		super(enm);
		m_module = module;
	}

	private final Module m_module;

}
