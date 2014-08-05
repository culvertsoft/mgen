package se.culvertsoft.mgen.javapack.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public class DeepCopyer {

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PUBLIC API - - - - - - - - - - -
	 * 
	 * *************************************************************/

	public static <T extends Enum<?>> T deepCopy(final T a, final Type type) {
		return a;
	}

	public static boolean deepCopy(final boolean a, final Type type) {
		return a;
	}

	public static boolean[] deepCopy(final boolean[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static byte deepCopy(final byte a, final Type type) {
		return a;
	}

	public static byte[] deepCopy(final byte[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static short deepCopy(final short a, final Type type) {
		return a;
	}

	public static short[] deepCopy(final short[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static int deepCopy(final int a, final Type type) {
		return a;
	}

	public static int[] deepCopy(final int[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static long deepCopy(final long a, final Type type) {
		return a;
	}

	public static long[] deepCopy(final long[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static float deepCopy(final float a, final Type type) {
		return a;
	}

	public static float[] deepCopy(final float[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static double deepCopy(final double a, final Type type) {
		return a;
	}

	public static double[] deepCopy(final double[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	public static String deepCopy(final String a, final Type type) {
		return a;
	}

	public static String[] deepCopy(final String[] a, final Type type) {
		return a != null ? Arrays.copyOf(a, a.length) : null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] deepCopy(final T[] a, final Type type) {
		return (T[]) deepCopyArray(a, (ArrayType) type);
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> deepCopy(final List<T> a, final Type type) {
		return (ArrayList<T>) deepCopyList(a, (ListType) type);
	}

	@SuppressWarnings("unchecked")
	public static <K, V> HashMap<K, V> deepCopy(final HashMap<K, V> a, final Type type) {
		return (HashMap<K, V>) deepCopyMap(a, (MapType) type);
	}

	@SuppressWarnings("unchecked")
	public static <T extends MGenBase> T deepCopy(final T a, final Type type) {
		return (T) deepCopyMGenObject(a);
	}

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PRIVATE HELPERS - - - - - - - - - - -
	 * 
	 * *************************************************************/

	private static Object deepCopyArray(final Object array, final ArrayType type) {
		if (array == null)
			return null;
		switch (type.elementType().typeEnum()) {
		case BOOL:
			return deepCopy((boolean[]) array, type);
		case INT8:
			return deepCopy((byte[]) array, type);
		case INT16:
			return deepCopy((short[]) array, type);
		case INT32:
			return deepCopy((int[]) array, type);
		case INT64:
			return deepCopy((long[]) array, type);
		case FLOAT32:
			return deepCopy((float[]) array, type);
		case FLOAT64:
			return deepCopy((double[]) array, type);
		case STRING:
			return deepCopy((String[]) array, type);
		default:
			final Class<?> compType = array.getClass().getComponentType();
			final int n = Array.getLength(array);
			final Object out = Array.newInstance(compType, n);
			for (int index = 0; index < n; index++) {
				Array.set(out, index, deepCopyObject(Array.get(array, index), type.elementType()));
			}
			return out;
		}
	}

	@SuppressWarnings("unchecked")
	private static Object deepCopyObject(final Object o, final Type type) {
		if (o == null)
			return null;
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
			return o;
		case ARRAY:
			return deepCopyArray(o, (ArrayType) type);
		case LIST:
			return deepCopyList((List<Object>) o, (ListType) type);
		case MAP:
			return deepCopyMap((HashMap<Object, Object>) o, (MapType) type);
		case CLASS:
			return deepCopyMGenObject((MGenBase) o);
		default:
			throw new RuntimeException(DeepCopyer.class
					+ ": deepCopyObject failed. Unrecognized type");
		}
	}

	private static Map<Object, Object> deepCopyMap(final HashMap<?, ?> src, final MapType type) {
		if (src == null)
			return null;
		final HashMap<Object, Object> out = new HashMap<Object, Object>(src.size());
		for (final Object key : src.keySet()) {
			final Object value = src.get(key);
			out.put(deepCopyObject(key, type.keyType()), deepCopyObject(value, type.valueType()));
		}
		return out;
	}

	private static ArrayList<Object> deepCopyList(final List<?> list, final ListType type) {
		if (list == null)
			return null;
		final ArrayList<Object> out = new ArrayList<Object>(list.size());
		for (final Object src : list)
			out.add(deepCopyObject(src, type.elementType()));
		return out;
	}

	private static MGenBase deepCopyMGenObject(final MGenBase o) {
		if (o == null)
			return null;
		return o.deepCopy();
	}

}
