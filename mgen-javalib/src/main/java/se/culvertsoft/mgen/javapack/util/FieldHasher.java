package se.culvertsoft.mgen.javapack.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public class FieldHasher {

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PUBLIC API - - - - - - - - - - -
	 * 
	 * *************************************************************/

	public static int calc(final Enum<?> e, final Type type) {
		return e != null ? e.hashCode() : 0;
	}

	public static int calc(final boolean a, final Type type) {
		return a ? 1231 : 1237;
	}

	public static int calc(final boolean[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final byte a, final Type type) {
		return (int) a;
	}

	public static int calc(final byte[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final short a, final Type type) {
		return (int) a;
	}

	public static int calc(final short[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final int a, final Type type) {
		return a;
	}

	public static int calc(final int[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final long a, final Type type) {
		return (int) (a ^ (a >>> 32));
	}

	public static int calc(final long[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final float a, final Type type) {
		return Float.floatToIntBits(a);
	}

	public static int calc(final float[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final double a, final Type type) {
		final long bits = Double.doubleToLongBits(a);
		return (int) (bits ^ (bits >>> 32));
	}

	public static int calc(final double[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final String a, final Type type) {
		return a == null ? 0 : a.hashCode();
	}

	public static int calc(final String[] a, final Type type) {
		return Arrays.hashCode(a);
	}

	public static int calc(final MGenBase a, final Type type) {
		return calcMgenObject(a, type);
	}

	public static <T> int calc(final T[] a, final Type type) {
		// Must be customized deep, think of Array<List<Array>>
		return calcObjectArray(a, (ArrayType) type);
	}

	public static <T> int calc(final List<T> a, final Type type) {
		// Must be customized deep, think of List<Array>
		return calcList(a, (ListType) type);
	}

	public static <K, V> int calc(final Map<K, V> a, final Type type) {
		// Must be customized deep, think of Map<Array,Array>
		return calcMap(a, (MapType) type);
	}

	/*
	 * public static int calc(final Object a, final Type type) { // Obviously
	 * need to be customized deep }
	 */
	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PRIVATE HELPERS - - - - - - - - - - -
	 * 
	 * *************************************************************/

	private static int calcMgenObject(final MGenBase a, final Type type) {
		return a == null ? 0 : a.hashCode();
	}

	private static <T> int calcObjectArray(final T[] a, final ArrayType type) {

		if (a == null)
			return 0;

		final Type elemType = type.elementType();

		int result = 1;

		for (Object element : a)
			result = 31 * result
					+ (element == null ? 0 : calcObject(element, elemType));

		return result;
	}

	private static int calcArray(final Object a, final ArrayType arrayType) {

		if (a == null)
			return 0;

		switch (arrayType.elementType().typeEnum()) {
		case BOOL:
			return calc((boolean[]) a, arrayType);
		case INT8:
			return calc((byte[]) a, arrayType);
		case INT16:
			return calc((short[]) a, arrayType);
		case INT32:
			return calc((int[]) a, arrayType);
		case INT64:
			return calc((long[]) a, arrayType);
		case FLOAT32:
			return calc((float[]) a, arrayType);
		case FLOAT64:
			return calc((double[]) a, arrayType);
		case STRING:
			return calc((String[]) a, arrayType);
		default:
			return calc((Object[]) a, arrayType);
		}
	}

	private static int calcObject(final Object a, final Type type) {

		if (a == null)
			return 0;

		switch (type.typeEnum()) {
		case ENUM:
			return a.hashCode();
		case BOOL:
			return calc(((Boolean) a), type);
		case INT8:
			return calc(((Byte) a), type);
		case INT16:
			return calc(((Short) a), type);
		case INT32:
			return calc(((Integer) a), type);
		case INT64:
			return calc(((Long) a), type);
		case FLOAT32:
			return calc(((Float) a), type);
		case FLOAT64:
			return calc(((Double) a), type);
		case STRING:
			return calc(((String) a), type);
		case ARRAY:
			return calcArray(a, (ArrayType) type);
		case LIST:
			return calcList((List<?>) a, (ListType) type);
		case MAP:
			return calcMap((Map<?, ?>) a, (MapType) type);
		case CLASS:
		case UNKNOWN:
			return calcMgenObject((MGenBase) a, type);
		}

		return a.hashCode();
	}

	private static int calcMap(final Map<?, ?> a, final MapType type) {

		if (a == null)
			return 0;

		int h = 0;

		final Type keyType = type.keyType();
		final Type valueType = type.valueType();

		for (final Map.Entry<?, ?> e : a.entrySet()) {
			h += (e.getKey() == null ? 0 : calcObject(e.getKey(), keyType))
					^ (e.getValue() == null ? 0 : calcObject(
							e.getValue(),
							valueType));
		}

		return h;

	}

	private static int calcList(final List<?> a, final ListType listType) {

		if (a == null)
			return 0;

		int result = 1;

		final Type elemType = listType.elementType();

		for (final Object element : a)
			result = 31 * result
					+ (element == null ? 0 : calcObject(element, elemType));

		return result;

	}

}
