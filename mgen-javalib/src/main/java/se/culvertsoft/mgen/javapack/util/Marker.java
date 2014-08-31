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
 * Utility class to be used internally by generated code only. Used for marking
 * fields of MGen objects as set. Used for testing.
 */
public class Marker {

	public static void setFieldSetDeep(final MGenBase object, final Type type) {
		if (object == null)
			return;
		object._setAllFieldsSet(true, FieldSetDepth.DEEP);
	}

	public static <T> void setFieldSetDeep(final T[] a, final Type type) {
		setArrayDeep(a, (ArrayType) type);
	}

	public static <T> void setFieldSetDeep(final List<T> a, final Type type) {
		setEntries(a, ((ListType) type).elementType());
	}

	public static
			<K, V>
			void
			setFieldSetDeep(final Map<K, V> a, final Type type) {
		setMap(a, (MapType) type);
	}

	public static void setFieldSetDeep(final Object a, final Type type) {
		setObjectDeep(a, type);
	}

	// ///////////////////// PRIVATE HELPERS //////////////////////////

	private static void setObjectDeep(final Object a, final Type type) {
		if (a == null)
			return;
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
			return;
		case ARRAY:
			setArray(a, (ArrayType) type);
			break;
		case LIST:
			setEntries((List<?>) a, ((ListType) type).elementType());
			break;
		case MAP:
			setMap((Map<?, ?>) a, (MapType) type);
			break;
		case CLASS:
		case UNKNOWN:
			setFieldSetDeep((MGenBase) a, null);
			break;
		}

	}

	private static <T> void setArrayDeep(final T[] a, final ArrayType type) {
		if (a == null)
			return;
		for (final T t : a) {
			setObjectDeep(t, type.elementType());
		}
	}

	private static void setArray(final Object a, final ArrayType arrayType) {
		if (a == null)
			return;
		switch (arrayType.elementType().typeEnum()) {
		case BOOL:
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
		case STRING:
			return;
		default:
			setArrayDeep((Object[]) a, arrayType);
			break;
		}
	}

	private static void setMap(final Map<?, ?> a, final MapType type) {
		if (a == null)
			return;
		setEntries(a.keySet(), ((MapType) type).keyType());
		setEntries(a.values(), ((MapType) type).valueType());
	}

	private static void setEntries(final Collection<?> a, final Type elemType) {
		if (a == null)
			return;
		switch (elemType.typeEnum()) {
		case BOOL:
		case INT8:
		case INT16:
		case INT32:
		case INT64:
		case FLOAT32:
		case FLOAT64:
		case STRING:
			return;
		default:
			for (final Object o : a) {
				setObjectDeep(o, elemType);
			}
		}
	}
}
