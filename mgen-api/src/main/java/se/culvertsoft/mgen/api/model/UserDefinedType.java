package se.culvertsoft.mgen.api.model;

/**
 * Base class for user defined types. User defined types includes MGen object
 * classes and enumerations. See ClassType and EnumType.
 */
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
