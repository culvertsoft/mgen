package se.culvertsoft.mgen.javapack.serialization;

import java.util.ArrayList;
import java.util.List;

import se.culvertsoft.mgen.api.model.Field;
import se.culvertsoft.mgen.api.model.UnknownCustomType;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.exceptions.MissingRequiredFieldsException;
import se.culvertsoft.mgen.javapack.exceptions.UnexpectedTypeException;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;

public class BuiltInSerializerUtils {

	protected static void throwUnexpectTag(final String extraInfo,
			final byte expTag, final byte readTag) {
		throw new UnexpectedTypeException("Unexpected type tag. Expected "
				+ expTag + " but got " + readTag + ". " + extraInfo);
	}

	protected static void throwUnexpectType(final String extraInfo,
			final String expTypeName, final String typeName) {
		throw new UnexpectedTypeException("Unexpected type received. Expected "
				+ expTypeName + " but got " + typeName + ". " + extraInfo);
	}

	protected static void throwMissingReqFields(
			final List<Field> missingFields, final MGenBase object) {
		throw new MissingRequiredFieldsException("Missing required fields ["
				+ missingFields + "] on object '" + object + "'");
	}

	protected static void ensureNoMissingReqFields(final MGenBase object) {

		if (object != null && !object._validate(FieldSetDepth.SHALLOW)) {

			final ArrayList<Field> missingReqFields = new ArrayList<Field>();

			for (final Field f : object._fields()) {
				if (f.isRequired()
						&& !object._isFieldSet(f, FieldSetDepth.SHALLOW)) {
					missingReqFields.add(f);
				}
			}

			throw new MissingRequiredFieldsException(
					"Missing required fields [" + missingReqFields
							+ "] on object '" + object + "'");
		}
	}

	protected static void ensureExpectedType(final MGenBase object,
			final UnknownCustomType expectedType) {
		if (object != null && expectedType != null
				&& !object.isInstanceOfTypeWithId(expectedType.typeId())) {
			throwUnexpectType("", expectedType.writtenType(),
					object._typeName());
		}
	}

}
