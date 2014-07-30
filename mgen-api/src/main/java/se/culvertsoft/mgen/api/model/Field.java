package se.culvertsoft.mgen.api.model;

import java.util.List;

/**
 * Represents a field of a custom type(class). Field objects exist both during
 * compilation in the mgen compiler, and also when using generated mgen code.
 * 
 * When using mgen generated types, the primary purpose of metadata fields (of
 * this type - Field) compensate for java generics type erasure.
 */
public interface Field {

	/**
	 * The name of this field (the member name).
	 */
	String name();

	/**
	 * The type of this field
	 */
	Type typ();

	/**
	 * The flags of this field
	 */
	List<String> flags();

	/**
	 * The default value specified for this field, or null if no default value
	 * is specified
	 */
	DefaultValue defaultValue();

	/**
	 * The 16 bit id of this field
	 */
	short id();

	/**
	 * The base 64 representation of this field's id.
	 */
	String idBase64();

	/**
	 * Convenience method to query if the user specified his/her own field id in
	 * the IDl, instead of letting the compiler generate one for them.
	 */
	boolean hasIdOverride();

	/**
	 * True if this field was marked as required in the IDl.
	 */
	boolean isRequired();

	/**
	 * True if this field was marked as polymorphic in the IDL. This has no
	 * effect on languages such as Java where are fields are polymorphic. It
	 * mainly affects languages such as C++ which require special handling of
	 * polymorphic class members.
	 */
	boolean isPolymorphic();

	/**
	 * True if this field was marked as parked in the IDL. A parked field
	 * occupies a field id, but the generators should not generate any code for
	 * it.
	 */
	boolean isParked();

	/**
	 * True if this field was marked as transient in the IDL. Transient fields
	 * are not serialized by mgen serializers. If you wish to serialize default
	 * fields you must either extends the mgen serializers or use your own.
	 */
	boolean isTransient();

	/**
	 * True if this field is linked. When the mgen compiler executes, it
	 * analyses the IDL in two passes. In the first pass field objects are
	 * created but custom types, enums and default values are only referenced by
	 * name - we call this unlinked. The second pass of the compiler links these
	 * names to actual type objects and creates (recursive) default value
	 * representations. When this has been done the field is considered linked.
	 * 
	 * Calling this function on generated code (outside the compiler) always
	 * returns false.
	 * 
	 */
	boolean isLinked();

	/**
	 * Returns true if this field has a default value specified in the IDL.
	 * 
	 * Calling this function on generated code (outside the compiler) always
	 * returns false.
	 */
	boolean hasDefaultValue();

	/**
	 * Creates a new Field identical to this field, except for having a new
	 * type.
	 */
	Field transform(final Type type);

	/**
	 * Creates a new Field identical to this field, except for having a new
	 * default value.
	 */
	Field transform(final DefaultValue v);

}
