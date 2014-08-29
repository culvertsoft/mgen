package se.culvertsoft.mgen.javapack.classes;

import java.io.IOException;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;
import se.culvertsoft.mgen.javapack.metadata.FieldVisitSelection;
import se.culvertsoft.mgen.javapack.serialization.FieldVisitor;
import se.culvertsoft.mgen.javapack.serialization.Reader;

/**
 * MGenBase is the base class of all classes generated with MGen. This base
 * class provides the interface methods required to facilitate object
 * visitation, dynamic double dispatch and serialization.
 * 
 * It also provides several additional methods for getting metadata about this
 * object and the class of this object.
 */
public abstract class MGenBase {

	/**
	 * Generated toString() method. The generated default implementation simply
	 * uses the MGen Json Serializer (JsonPrettyWriter).
	 * 
	 * @return This object in JSON.
	 */
	public abstract String toString();

	/**
	 * Generated equals(..) method for all MGen objects.
	 * 
	 * @return If this method is deeply equal to provided parameter.
	 */
	public abstract boolean equals(final Object other);

	/**
	 * Generated hashCode method for all MGen objects.
	 * 
	 * @return The deeply calculated hashcode for this MGen object.
	 */
	public abstract int hashCode();

	/**
	 * Creates a new deep copy of this object.
	 * 
	 * @return A new deeply copied instance of this object.
	 */
	public abstract MGenBase deepCopy();

	/**
	 * Gets the 64 bit type id of the class of this object.
	 * 
	 * @return The 64 bit type id of the class of this object.
	 */
	public abstract long _typeId();

	/**
	 * Gets the 64 bit type ids of the class hierarchy of this object.
	 * 
	 * @return The 64 bit type ids of the class hierarchy of this object.
	 */
	public abstract long[] _typeIds();

	/**
	 * Gets the 16 bit type id of this class in its class hierarchy. This id is
	 * ONLY unique among classes with the same super type as this class.
	 * 
	 * @return The 16 bit type id of this class in its class hierarchy.
	 */
	public abstract short _typeId16Bit();

	/**
	 * Gets the 16 bit type ids of the class hierarchy of this object.
	 * 
	 * @return The 16 bit type ids of the class hierarchy of this object.
	 */
	public abstract short[] _typeIds16Bit();

	/**
	 * Gets the 16 bit type id of this class in its class hierarchy, in base64
	 * format. This id is ONLY unique among classes with the same super type as
	 * this class.
	 * 
	 * @return The 16 bit type id of this class in its class hierarchy, in
	 *         base64 format.
	 */
	public abstract String _typeId16BitBase64();

	/**
	 * Gets the 16 bit type ids of the class hierarchy of this object, in base
	 * 64 format.
	 * 
	 * @return The 16 bit type ids of the class hierarchy of this object, in
	 *         base 64 format.
	 */
	public abstract String[] _typeIds16BitBase64();

	/**
	 * Gets the 16 bit type ids of the class hierarchy of this object, in base64
	 * format, concatenated into a single string.
	 * 
	 * @return The 16 bit type ids of the class hierarchy of this object, in
	 *         base64 format, concatenated into a single string.
	 */
	public abstract String _typeIds16BitBase64String();

	/**
	 * Gets the qualified name of the class of this object.
	 * 
	 * @return The qualified name of the class of this object.
	 */
	public abstract String _typeName();

	/**
	 * Gets the qualified names of the class hierarchy of this object.
	 * 
	 * @return The qualified names of the class hierarchy of this object.
	 */
	public abstract String[] _typeNames();

	/**
	 * Gets Field metadata objects describing the fields of the class of this
	 * object.
	 * 
	 * @return Field metadata objects describing the fields of the class of this
	 *         object.
	 */
	public abstract Field[] _fields();

	/**
	 * Gets a Field metadata object describing a particular field of the class
	 * of this object.
	 * 
	 * @param id
	 *            The id of the field to get metadata about.
	 * 
	 * @return A Field metadata object describing a particular field of the
	 *         class of this object, or null if none is found.
	 */
	public abstract Field _fieldById(final short id);

	/**
	 * Gets a Field metadata object describing a particular field of the class
	 * of this object.
	 * 
	 * @param name
	 *            The name of the field to get metadata about.
	 * 
	 * @return A Field metadata object describing a particular field of the
	 *         class of this object, or null if none is found.
	 */
	public abstract Field _fieldByName(final String name);

	/**
	 * Counts the number of fields currently set on this object. This is used
	 * when serialization visitors visit this object and need to prepend the
	 * writing of an object on a stream with the number of fields that follow,
	 * before writing them.
	 * 
	 * @param fieldSetDepth
	 *            What depth to check the fields on this object
	 *            (FieldSetDepth.DEEP or FieldSetDepth.SHALLOW).
	 * 
	 * @param includeTransient
	 *            If to include fields marked as transient in the count.
	 * 
	 * @return The number of fields currently set on this object.
	 */
	public abstract int _nFieldsSet(
			final FieldSetDepth fieldSetDepth,
			final boolean includeTransient);

	/**
	 * Checks if a particular field is set on this object.
	 * 
	 * @param field
	 *            The field to check.
	 * 
	 * @param depth
	 *            The depth to check (FieldSetDepth.DEEP or
	 *            FieldSetDepth.SHALLOW).
	 * 
	 * @return If the field is set on this object.
	 */
	public abstract boolean _isFieldSet(final Field field, final FieldSetDepth depth);

	/**
	 * Convenience method primarily for testing. Marks all fields on this object
	 * as set or not set.
	 * 
	 * @param state
	 *            The state to mark fields to.
	 * 
	 * @param depth
	 *            The depth to mark fields to (FieldSetDepth.DEEP or
	 *            FieldSetDepth.SHALLOW).
	 * 
	 * @return this object.
	 */
	public abstract MGenBase _setAllFieldsSet(final boolean state, final FieldSetDepth depth);

	/**
	 * Checks that all required fields on this object are set.
	 * 
	 * @param depth
	 *            The depth to perform the check to (FieldSetDepth.DEEP or
	 *            FieldSetDepth.SHALLOW).
	 * 
	 * @return If all required fields on this object are set.
	 */
	public abstract boolean _validate(final FieldSetDepth depth);

	/**
	 * Method for visiting this object with a FieldVisitor.
	 * 
	 * @param visitor
	 *            The visitor to use.
	 * 
	 * @param fieldSelection
	 *            Selection of what fields to visit.
	 * 
	 * @throws IOException
	 *             The FieldVisitor interface is specified as throwing
	 *             IOExceptions, so this method must also be declared as
	 *             throwing.
	 */
	public abstract void _accept(
			final FieldVisitor visitor,
			final FieldVisitSelection fieldSelection) throws IOException;

	/**
	 * Method called while reading the fields of an object from a data stream.
	 * When reading an object from stream, fields may appear in any order
	 * depending on the serialization format and also if the developer reorders
	 * the field in an object between different code versions. This method is
	 * what connects the field in the stream to the field on the object being
	 * read.
	 * 
	 * @param fieldId
	 *            The id of the field that is being read
	 * 
	 * @param context
	 *            An optional parameter that may be passed by the reader to
	 *            himself (will be passed back to the reader.readField(..)
	 *            call).
	 * 
	 * @param reader
	 *            The reader which expects a callback in the form of
	 *            reader.readField(..)
	 * 
	 * @return True if the field was found
	 * 
	 * @throws IOException
	 *             The Reader interface is specified as throwing IOExceptions,
	 *             so this method must also be declared as throwing.
	 */
	public abstract boolean _readField(
			final short fieldId,
			final Object context,
			final Reader reader) throws IOException;

}
