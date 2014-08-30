package se.culvertsoft.mgen.javapack.util;

import java.util.ArrayList;
import java.util.List;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.MissingRequiredFieldsException;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;

/**
 * Class holding utility methods used by MGen's built in readers and writers.
 * Used for validating objects and throwing exceptions with useful error
 * messages. Only intended to be used internally by the MGen built-in
 * serializers.
 */
public class BuiltInSerializerUtils {

	/**
	 * Throws an UnexpectedTypeException with a useful error message.
	 * 
	 * @param extraInfo
	 *            Extra text to append to the error message
	 * 
	 * @param expTag
	 *            The type tag expected
	 * 
	 * @param readTag
	 *            The type tag actually read
	 */
	public static void throwUnexpectTag(
			final String extraInfo,
			final byte expTag,
			final byte readTag) {
		throw new UnexpectedTypeException("Unexpected type tag. Expected " + expTag + " but got "
				+ readTag + ". " + extraInfo);
	}

	/**
	 * Throws an UnexpectedTypeException with a useful error message.
	 * 
	 * @param extraInfo
	 *            Extra text to append to the error message
	 * 
	 * @param expTypeName
	 *            The expected type name
	 * 
	 * @param typeName
	 *            The type name of the object actually read back
	 */
	public static void throwUnexpectType(
			final String extraInfo,
			final String expTypeName,
			final String typeName) {
		throw new UnexpectedTypeException("Unexpected type received. Expected " + expTypeName
				+ " but got " + typeName + ". " + extraInfo);
	}

	/**
	 * Throws a MissingRequiredFieldsException with a useful error message.
	 * 
	 * @param missingFields
	 *            The required fields that are missing
	 * 
	 * @param object
	 *            The objects they are missing on
	 */
	public static
			void
			throwMissingReqFields(final List<Field> missingFields, final MGenBase object) {
		throw new MissingRequiredFieldsException("Missing required fields [" + missingFields
				+ "] on object '" + object + "'");
	}

	/**
	 * Checks that the MGen object provided has no missing required fields, and
	 * if it does, throws a MissingRequiredFieldsException.
	 * 
	 * @param object
	 *            The MGen object to test
	 * 
	 * @param depth
	 *            If to test the object DEEP or SHALLOW
	 */
	public static void ensureNoMissingReqFields(final MGenBase object, final FieldSetDepth depth) {

		if (object != null && !object._validate(depth)) {

			final ArrayList<Field> missingReqFields = new ArrayList<Field>();

			for (final Field f : object._fields()) {
				if (f.isRequired() && !object._isFieldSet(f, depth)) {
					missingReqFields.add(f);
				}
			}

			throw new MissingRequiredFieldsException("Missing required fields [" + missingReqFields
					+ "] on object '" + object + "'");
		}
	}

	/**
	 * Checks that the MGen object provided has no missing required fields, and
	 * if it does, throws a MissingRequiredFieldsException. The test is
	 * performed SHALLOW.
	 * 
	 * @param object
	 *            The MGen object to test
	 */
	public static void ensureNoMissingReqFields(final MGenBase object) {
		ensureNoMissingReqFields(object, FieldSetDepth.SHALLOW);
	}

}
