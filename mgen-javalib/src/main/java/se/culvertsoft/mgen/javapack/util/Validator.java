package se.culvertsoft.mgen.javapack.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.MGenBase;
import se.culvertsoft.mgen.javapack.metadata.FieldSetDepth;

/**
 * Utility class for validating (checking for missing required fields) MGen
 * objects before being written by writers and after being read by readers. Not
 * intended to be used except from MGen's built-in readers and writers.
 */
public class Validator {

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PUBLIC API - - - - - - - - - - -
	 * 
	 * *************************************************************/

	public static boolean validateFieldDeep(final MGenBase object, final Type type) {
		return object == null || object._validate(FieldSetDepth.DEEP);
	}

	public static <T> boolean validateFieldDeep(final T[] a, final Type type) {
		return validateArrayDeep(a, (ArrayType) type);
	}

	public static <T> boolean validateFieldDeep(final List<T> a, final Type type) {
		return validateEntries(a, ((ListType) type).elementType());
	}

	public static <K, V> boolean validateFieldDeep(final Map<K, V> a, final Type type) {
		return validateMap(a, (MapType) type);
	}

	public static boolean validateFieldDeep(final Object a, final Type type) {
		return validateObjectDeep(a, type);
	}

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PRIVATE HELPERS - - - - - - - - - - -
	 * 
	 * *************************************************************/

	private static boolean validateObjectDeep(final Object a, final Type type) {
		if (a == null)
			return true;
		switch (type.typeEnum()) {
		case ENUM:
		case BOOL:
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
		case STRING:
			return true;
		case ARRAY:
			return validateArray(a, (ArrayType) type);
		case LIST:
			return validateEntries((List<?>) a, ((ListType) type).elementType());
		case MAP:
			return validateMap((Map<?, ?>) a, (MapType) type);
		case CLASS:
		case UNKNOWN:
			return validateFieldDeep((MGenBase) a, null);
		}

		return false;
	}

	private static <T> boolean validateArrayDeep(final T[] a, final ArrayType type) {
		if (a == null)
			return true;
		for (final T t : a) {
			if (!validateObjectDeep(t, type.elementType())) {
				return false;
			}
		}
		return true;
	}

	private static boolean validateArray(final Object a, final ArrayType arrayType) {
		if (a == null)
			return true;
		switch (arrayType.elementType().typeEnum()) {
		case BOOL:
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
		case STRING:
			return true;
		default:
			return validateArrayDeep((Object[]) a, arrayType);
		}
	}

	private static boolean validateMap(final Map<?, ?> a, final MapType type) {
		if (a == null)
			return true;
		return validateEntries(a.keySet(), ((MapType) type).keyType())
				&& validateEntries(a.values(), ((MapType) type).valueType());
	}

	private static boolean validateEntries(final Collection<?> a, final Type elemType) {
		if (a == null)
			return true;
		switch (elemType.typeEnum()) {
		case BOOL:
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
		case STRING:
			return true;
		default:
			for (final Object o : a) {
				if (!validateObjectDeep(o, elemType)) {
					return false;
				}
			}
			return true;
		}
	}

}
