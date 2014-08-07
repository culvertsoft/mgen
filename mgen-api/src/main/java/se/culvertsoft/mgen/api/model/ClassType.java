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
 * A class definition, intended to be used inside the compiler.
 */
public class ClassType extends UserDefinedType {

	/**
	 * 64 bit id hashed from the qualified type name of this class
	 */
	public long typeId() {
		return CRC64.calc(m_fullName);
	}

	/**
	 * 16 bit id hashed from the qualified type name of this class. Verified by
	 * the compiler to be unique among classes with the same super type.
	 */
	public short typeId16Bit() {
		return m_id16Bit;
	}

	/**
	 * The base64 representation of typeId16Bit()
	 */
	public String typeId16BitBase64() {
		return Base64.encode(typeId16Bit());
	}

	/**
	 * The super type of this class, or null if it has none
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public UserDefinedType superType() {
		return m_superType;
	}

	/**
	 * the base 16 bit type concatinated with all parents 16 bits hash.
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
	 * True if this type has a super type
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public boolean hasSuperType() {
		return superType() != null;
	}

	/**
	 * True if this type has sub types
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public boolean hasSubTypes() {
		return !subTypes().isEmpty();
	}

	/**
	 * Returns the sub types of this class
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public List<ClassType> subTypes() {
		return m_subTypes;
	}

	/**
	 * Returns all (recursive) super types of this class
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
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
	 * Returns all fields of this class, excluding those of its super type(s).
	 * Returns an empty list of called from outside the compiler.
	 */
	public List<Field> fields() {
		return m_fields;
	}

	/**
	 * Returns all fields of this class, including those of its super type(s).
	 * Returns an empty list of called outside the compiler.
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
	 * Returns the constants defined within this class.
	 * 
	 * @return the constants defined within this class.
	 */
	public List<Constant> constants() {
		return m_constants;
	}

	/**
	 * Adds a constant to this class.
	 * 
	 * @param c
	 *            the constant to add.
	 */
	public void addConstant(final Constant c) {
		m_constants.add(c);
	}

	/**
	 * Sets the constants of this class.
	 * 
	 * @param constants
	 *            the constants of this class.
	 */
	public void setConstants(final List<Constant> constants) {
		m_constants = constants;
	}

	/**
	 * Adds constants to this class.
	 * 
	 * @param constants
	 *            the constants to add.
	 */
	public void addConstants(final Collection<Constant> constants) {
		m_constants.addAll(constants);
	}

	/**
	 * Returns the set of types referenced from this type.
	 * 
	 * @throws RuntimeException
	 *             If called outside the compiler
	 */
	public Set<ClassType> referencedClasses() {
		if (m_referencedClasses == null)
			findReferences();
		return m_referencedClasses;
	}

	/**
	 * Returns the set of types referenced from this type.
	 */
	public java.util.Set<EnumType> referencedEnums() {
		if (m_referencedEnums == null)
			findReferences();
		return m_referencedEnums;
	}

	/**
	 * Finds/Gets a field by name. Returns null if no such field is found.
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
	 * Finds/Gets a constant by name. Returns null if no such field is found.
	 * The search checks both the short and full name of all constants within
	 * this class.
	 */
	public Constant findConstant(final String name) {
		for (final Constant c : m_constants) {
			if (c.shortName().equals(name) || c.fullName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public boolean isLinked() {
		return true;
	}

	public void setSuperType(final ClassType superType) {
		m_superType = superType;
	}

	public void setFields(final List<Field> fields) {
		m_fields = fields;
	}

	public void addField(final Field field) {
		m_fields.add(field);
	}

	@Override
	public String fullName() {
		return m_fullName;
	}

	@Override
	public String shortName() {
		return m_name;
	}

	@Override
	public Class<?> classOf() {
		return null;
	}

	@Override
	public String toString() {
		return m_fullName;
	}

	@Override
	public boolean containsUserDefinedType() {
		return true;
	}

	/**
	 * Add a derived type (for internal compiler linkage). Do not use this. It
	 * will be called by the compiler.
	 */
	public ClassType addSubType(final ClassType t) {
		m_subTypes.add(t);
		return this;
	}

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

	private void findReferences(
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

	public ClassType(final String name, final Module module, final UserDefinedType superType) {
		this(name, module, CRC16.calc(module.path() + "." + name), superType);
	}

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

}
