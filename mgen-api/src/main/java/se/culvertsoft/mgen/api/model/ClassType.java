package se.culvertsoft.mgen.api.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import se.culvertsoft.mgen.api.util.Base64;
import se.culvertsoft.mgen.api.util.CRC16;
import se.culvertsoft.mgen.api.util.CRC64;

/**
 * Represents a custom defined MGen class. ClassType is used inside the
 * compiler, as opposed to RuntimeClassType is used during application runtime.
 */
public class ClassType extends UserDefinedType {

	/**
	 * Returns the 64 bit type id of the class that this ClassType represents
	 * 
	 * @return The 64 bit type id of the class that this ClassType represents
	 */
	public long typeId() {
		return CRC64.calc(m_fullName);
	}

	/**
	 * Returns the 16 bit type id of the class that this ClassType represents.
	 * Note that this value is only required to be unique among classes with the
	 * same super type.
	 * 
	 * @return The 16 bit type id of the class that this ClassType represents
	 */
	public short typeId16Bit() {
		return m_id16Bit;
	}

	/**
	 * Returns the 16 bit type id of the class that this ClassType represents,
	 * in base64 form. Note that this value is only required to be unique among
	 * classes with the same super type.
	 * 
	 * @return The 16 bit type id of the class that this ClassType represents,
	 *         in base64 form
	 */
	public String typeId16BitBase64() {
		return Base64.encode(typeId16Bit());
	}

	/**
	 * Returns the super type of the class that this ClassType represents, or
	 * null if it doesn't have a super type.
	 * 
	 * @return The super type of the class that this ClassType represents, or
	 *         null if it doesn't have a super type.
	 */
	public UserDefinedType superType() {
		return m_superType;
	}

	/**
	 * Gets the 16 bit type id hierarchy of the class that this ClassType
	 * represents, in base64 form, concatenated into a single string.
	 * 
	 * @return The 16 bit type id hierarchy of the class that this ClassType
	 *         represents, in base64 form, concatenated into a single string.
	 */
	public String typeId16BitBase64Hierarchy() {

		if (m_typeId16BitBase64Heirarchy == null) {

			String s = "";

			for (ClassType customType : typeHierarchy())
				s += customType.typeId16BitBase64();

			m_typeId16BitBase64Heirarchy = s;
		}

		return m_typeId16BitBase64Heirarchy;
	}

	/**
	 * Checks if the class that this ClassType represents has a super type
	 * 
	 * @return If the class that this ClassType represents has a super type
	 */
	public boolean hasSuperType() {
		return superType() != null;
	}

	/**
	 * Checks if the class that this ClassType represents has any sub types
	 * 
	 * @return If the class that this ClassType represents has any sub types
	 */
	public boolean hasSubTypes() {
		return !subTypes().isEmpty();
	}

	/**
	 * Returns the sub types of the class that this ClassType represents
	 * 
	 * @return The sub types of the class that this ClassType represents
	 */
	public List<ClassType> subTypes() {
		return m_subTypes;
	}

	/**
	 * Returns the type hierarchy of the class that this ClassType represents
	 * (all super types and own type).
	 * 
	 * @return The type hierarchy of the class that this ClassType represents
	 */
	public List<ClassType> typeHierarchy() {

		if (m_typeHierarchy == null) {

			final List<ClassType> l = new ArrayList<ClassType>();

			if (hasSuperType())
				l.addAll(((ClassType) superType()).typeHierarchy());

			l.add(this);

			m_typeHierarchy = l;

		}

		return m_typeHierarchy;
	}

	/**
	 * Returns all fields of the class that this ClassType represents, excluding
	 * those of its super type(s). Returns an empty list of called from outside
	 * the compiler.
	 * 
	 * @return All fields of the class that this ClassType represent
	 */
	public List<Field> fields() {
		return m_fields;
	}

	/**
	 * Returns all fields of the class that this ClassType represents, including
	 * those of its super type(s). Returns an empty list of called outside the
	 * compiler.
	 * 
	 * @return All fields of the class that this ClassType represents
	 */
	public List<Field> fieldsInclSuper() {

		if (m_fieldsInclSuper == null) {

			m_fieldsInclSuper = new ArrayList<Field>();

			if (hasSuperType()) {
				m_fieldsInclSuper.addAll(((ClassType) superType()).fieldsInclSuper());
			}

			m_fieldsInclSuper.addAll(m_fields);

		}

		return m_fieldsInclSuper;
	}

	/**
	 * Returns the constants defined within the class that this ClassType
	 * represents.
	 * 
	 * @return the constants defined within the class that this ClassType
	 *         represents.
	 */
	public List<Constant> constants() {
		return m_constants;
	}

	/**
	 * Adds a constant to the class that this ClassType represesents.
	 * 
	 * @param c
	 *            the constant to add.
	 */
	public void addConstant(final Constant c) {
		m_constants.add(c);
	}

	/**
	 * Sets the constants of the class that this ClassType represents.
	 * 
	 * @param constants
	 *            the constants of the class that this ClassType represents.
	 */
	public void setConstants(final List<Constant> constants) {
		m_constants = constants;
	}

	/**
	 * Adds constants to the class that this ClassType represents.
	 * 
	 * @param constants
	 *            the constants to add.
	 */
	public void addConstants(final Collection<Constant> constants) {
		m_constants.addAll(constants);
	}

	/**
	 * Returns the set of types referenced from the class that this ClassType
	 * represents.
	 * 
	 * @return The set of types referenced from the class that this ClassType
	 *         represents.
	 */
	public Set<ClassType> referencedClasses() {
		if (m_referencedClasses == null)
			findReferences();
		return m_referencedClasses;
	}

	/**
	 * Returns the set of types referenced from the class that this ClassType
	 * represents.
	 * 
	 * @return The set of types referenced from the class that this ClassType
	 *         represents.
	 */
	public java.util.Set<EnumType> referencedEnums() {
		if (m_referencedEnums == null)
			findReferences();
		return m_referencedEnums;
	}

	/**
	 * Finds a field by name defined in the class that this ClassType
	 * represents. Returns null if no such field is found.
	 * 
	 * @param name
	 *            The short or long qualified name of the field
	 * 
	 * @return The field found by name, null if no field is found.
	 */
	public Field findField(final String name) {
		for (final Field f : fieldsInclSuper()) {
			if (f.name().equals(name)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * Finds a constant by name defined in the class that this ClassType
	 * represents. Returns null if no such field is found. The search checks
	 * both the short and full name of all constants.
	 * 
	 * @param name
	 *            The short or long qualified name of the constant
	 * 
	 * @return The constant found, or null if no constant was found
	 */
	public Constant findConstant(final String name) {
		for (final Constant c : m_constants) {
			if (c.shortName().equals(name) || c.qualifiedShortName().equals(name)
					|| c.fullName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isLinked() {
		return true;
	}

	/**
	 * Sets the super type of the class that this ClassType represents
	 * 
	 * @param superType
	 *            The super type to set
	 */
	public void setSuperType(final ClassType superType) {
		m_superType = superType;
	}

	/**
	 * Sets the fields of the class that this ClassType represents.
	 * 
	 * @param fields
	 *            The new fields of the class that this ClassType represents
	 */
	public void setFields(final List<Field> fields) {
		m_fields = fields;
	}

	/**
	 * Adds a field to the class that this ClassType represents.
	 * 
	 * @param field
	 *            The new field for the class that this ClassType represents.
	 */
	public void addField(final Field field) {
		m_fields.add(field);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String fullName() {
		return m_fullName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String shortName() {
		return m_name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> classOf() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return m_fullName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsUserDefinedType() {
		return true;
	}

	/**
	 * Adds a sub type to the class that this ClassType represents. This method
	 * should not be called directly, as it will be used by the compiler
	 * automatically during the linkage phase.
	 * 
	 * @param t
	 *            The new subtype to add
	 * 
	 * @return This ClassType
	 */
	public ClassType addSubType(final ClassType t) {
		m_subTypes.add(t);
		return this;
	}

	/**
	 * Internal helper method for finding all the types that this ClassType has
	 * references to.
	 */
	private void findReferences() {

		final Set<EnumType> enums = new LinkedHashSet<EnumType>();
		final Set<ClassType> classes = new LinkedHashSet<ClassType>();

		if (hasSuperType())
			classes.add((ClassType) superType());

		for (final Field f : m_fields)
			findReferences(f.typ(), classes, enums);

		for (final Constant c : m_constants)
			findReferences(c.typ(), classes, enums);

		m_referencedEnums = enums;
		m_referencedClasses = classes;
	}

	/**
	 * Internal helper method for finding all the types that a particular type
	 * has references to.
	 * 
	 * @param t
	 *            The type to find all references from
	 * 
	 * @param classes
	 *            The referenced classes found so far
	 * 
	 * @param enums
	 *            The referenced enums found so far
	 */
	private static void findReferences(
			final Type t,
			final Set<ClassType> classes,
			final Set<EnumType> enums) {

		switch (t.typeEnum()) {
		case ENUM:
			enums.add((EnumType) t);
			break;
		case CLASS:
			classes.add((ClassType) t);
			break;
		case ARRAY:
			findReferences(((ArrayType) t).elementType(), classes, enums);
			break;
		case LIST:
			findReferences(((ListType) t).elementType(), classes, enums);
			break;
		case MAP:
			findReferences(((MapType) t).keyType(), classes, enums);
			findReferences(((MapType) t).valueType(), classes, enums);
			break;
		default:
			break;
		}

	}

	/**
	 * Creates a new classType with an automatically assigned 16 bit type id.
	 * 
	 * @param name
	 *            The simple name of the class (without module name prepended)
	 * 
	 * @param module
	 *            The module wherein the class is defined
	 * 
	 * @param superType
	 *            The super type of the class
	 */
	public ClassType(final String name, final Module module, final UserDefinedType superType) {
		this(name, module, CRC16.calc(module.path() + "." + name), superType);
	}

	/**
	 * Creates a new classType with a a manually specified 16 bit type id.
	 * 
	 * @param name
	 *            The simple name of the class (without module name prepended)
	 * 
	 * @param module
	 *            The module wherein the class is defined
	 * 
	 * 
	 * @param id16Bit
	 *            The 16 bit type id of the class
	 * 
	 * @param superType
	 *            The super type of the class
	 */
	public ClassType(
			final String name,
			final Module module,
			final short id16Bit,
			final UserDefinedType superType) {
		super(TypeEnum.CLASS, module);
		m_name = name;
		m_fullName = module.path() + "." + m_name;
		m_typeId16BitBase64Heirarchy = null;
		m_id16Bit = id16Bit;
		m_superType = superType;
		m_fields = new ArrayList<Field>();
		m_constants = new ArrayList<Constant>();
		m_typeHierarchy = null;
		m_subTypes = new ArrayList<ClassType>();
		m_fieldsInclSuper = null;
		m_referencedClasses = null;
		m_referencedEnums = null;
	}

	private final String m_name;
	private final String m_fullName;
	private final short m_id16Bit;
	private UserDefinedType m_superType;
	private List<ClassType> m_typeHierarchy;
	private List<ClassType> m_subTypes;
	private List<Field> m_fields;
	private List<Constant> m_constants;

	private ArrayList<Field> m_fieldsInclSuper;
	private Set<ClassType> m_referencedClasses;
	private Set<EnumType> m_referencedEnums;
	private String m_typeId16BitBase64Heirarchy;

}
