package se.culvertsoft.mgen.api.model;

/**
 * Base class for user defined types. User defined types includes MGen object
 * classes and enumerations. See ClassType and EnumType.
 */
public abstract class UserDefinedType extends Type {

	/**
	 * Gets the Module that this type is defined within. Returns null if called
	 * from outside the compiler.
	 * 
	 * @return The Module that this type is defined within
	 */
	public Module module() {
		return m_module;
	}

	/**
	 * Creates a new user defined type
	 * 
	 * @param enm
	 *            The TypeEnum of this type, ENUM or CLASS
	 * 
	 * @param module
	 *            The module this UserDefinedType is defined within
	 */
	protected UserDefinedType(final TypeEnum enm, final Module module) {
		super(enm);
		m_module = module;
	}

	private final Module m_module;

}
