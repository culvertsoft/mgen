package se.culvertsoft.mgen.javapack.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import se.culvertsoft.mgen.api.model.ArrayType;
import se.culvertsoft.mgen.api.model.ListType;
import se.culvertsoft.mgen.api.model.MapType;
import se.culvertsoft.mgen.api.model.Type;
import se.culvertsoft.mgen.javapack.classes.MGenBase;

public class EqualityTester {

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PUBLIC API - - - - - - - - - - -
	 * 
	 * *************************************************************/

	public static boolean areEqual(
			final boolean a,
			final boolean b,
			final Type type) {
		return a == b;
	}

	public static boolean areEqual(
			final boolean[] a,
			final boolean[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static boolean areEqual(final byte a, final byte b, final Type type) {
		return a == b;
	}

	public static boolean areEqual(
			final byte[] a,
			final byte[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static
			boolean
			areEqual(final short a, final short b, final Type type) {
		return a == b;
	}

	public static boolean areEqual(
			final short[] a,
			final short[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static boolean areEqual(final int a, final int b, final Type type) {
		return a == b;
	}

	public static
			boolean
			areEqual(final int[] a, final int[] b, final Type type) {
		return Arrays.equals(a, b);
	}

	public static boolean areEqual(final long a, final long b, final Type type) {
		return a == b;
	}

	public static boolean areEqual(
			final long[] a,
			final long[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static
			boolean
			areEqual(final float a, final float b, final Type type) {
		return a == b;
	}

	public static boolean areEqual(
			final float[] a,
			final float[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static boolean areEqual(
			final double a,
			final double b,
			final Type type) {
		return a == b;
	}

	public static boolean areEqual(
			final double[] a,
			final double[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static boolean areEqual(
			final String a,
			final String b,
			final Type type) {
		if (a == null || b == null || a == b)
			return a == b;
		return a.equals(b);
	}

	public static boolean areEqual(
			final String[] a,
			final String[] b,
			final Type type) {
		return Arrays.equals(a, b);
	}

	public static
			<T>
			boolean
			areEqual(final T[] a, final T[] b, final Type type) {
		return areArraysEqual(a, b, (ArrayType) type);
	}

	public static <T> boolean areEqual(
			final List<T> a,
			final List<T> b,
			final Type type) {
		return areListsEqual(a, b, (ListType) type);
	}

	public static <K, V> boolean areEqual(
			final Map<K, V> a,
			final Map<K, V> b,
			final Type type) {
		return areMapsEqual(a, b, (MapType) type);
	}

	public static boolean areEqual(
			final MGenBase a,
			final MGenBase b,
			final Type type) {
		return areMgenObjectsEqual(a, b, type);
	}

	public static boolean areEqual(
			final Object a,
			final Object b,
			final Type type) {
		return areObjectsEqual(a, b, type);
	}

	/***************************************************************
	 * 
	 * 
	 * - - - - - - - - - - - PRIVATE HELPERS - - - - - - - - - - -
	 * 
	 * *************************************************************/

	private static boolean areMgenObjectsEqual(
			final MGenBase a,
			final MGenBase b,
			final Type type) {
		if (a == null || b == null || a == b)
			return a == b;
		if (!a.getClass().equals(b.getClass()))
			return false;
		return a.equals(b);
	}

	private static <T> boolean areArraysEqual(
			final T[] a,
			final T[] b,
			final ArrayType type) {
		if (a == null || b == null || a == b)
			return a == b;
		if (a.length != b.length)
			return false;
		for (int i = 0; i < a.length; i++) {
			if (!areEqual((Object) a[i], (Object) b[i], type.elementType())) {
				return false;
			}
		}
		return true;
	}

	private static boolean areArraysEqual(
			final Object a,
			final Object b,
			final ArrayType arrayType) {
		if (a == null || b == null || a == b)
			return a == b;
		if (a.getClass() != b.getClass())
			return false;
		switch (arrayType.elementType().typeEnum()) {
		case BOOL:
			return areEqual((boolean[]) a, (boolean[]) b, arrayType);
		case INT8:
			return areEqual((byte[]) a, (byte[]) b, arrayType);
		case INT16:
			return areEqual((short[]) a, (short[]) b, arrayType);
		case INT32:
			return areEqual((int[]) a, (int[]) b, arrayType);
		case INT64:
			return areEqual((long[]) a, (long[]) b, arrayType);
		case FLOAT32:
			return areEqual((float[]) a, (float[]) b, arrayType);
		case FLOAT64:
			return areEqual((double[]) a, (double[]) b, arrayType);
		case STRING:
			return areEqual((String[]) a, (String[]) b, arrayType);
		default:
			return areEqual((Object[]) a, (Object[]) b, arrayType);
		}
	}

	private static boolean areObjectsEqual(
			final Object a,
			final Object b,
			final Type type) {
		if (a == null || b == null || a == b)
			return a == b;
		if (!a.getClass().equals(b.getClass()))
			return false;

		switch (type.typeEnum()) {
		case BOOL:
			return ((Boolean) a).equals((Boolean) b);
		case INT8:
			return ((Byte) a).equals((Byte) b);
		case INT16:
			return ((Short) a).equals((Short) b);
		case INT32:
			return ((Integer) a).equals((Integer) b);
		case INT64:
			return ((Long) a).equals((Long) b);
		case FLOAT32:
			return ((Float) a).equals((Float) b);
		case FLOAT64:
			return ((Double) a).equals((Double) b);
		case STRING:
			return ((String) a).equals((String) b);
		case ARRAY:
			return areArraysEqual(a, b, (ArrayType) type);
		case LIST:
			return areListsEqual((List<?>) a, (List<?>) b, (ListType) type);
		case MAP:
			return areMapsEqual((Map<?, ?>) a, (Map<?, ?>) b, (MapType) type);
		case CUSTOM:
		case MGEN_BASE:
		case UNKNOWN:
			return areEqual((MGenBase) a, (MGenBase) b, type);
		}

		return a.equals(b);
	}

	private static boolean areMapsEqual(
			final Map<?, ?> a,
			final Map<?, ?> b,
			final MapType type) {
		if (a == null || b == null || a == b)
			return a == b;
		if (a.size() != b.size())
			return false;
		for (final Object key : a.keySet()) {
			final Object aValue = a.get(key);
			final Object bValue = b.get(key);
			if (!areObjectsEqual(aValue, bValue, type.valueType())) {
				return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private static boolean areListsEqual(
			final List<?> a,
			final List<?> b,
			final ListType type) {
		if (a == null || b == null || a == b)
			return a == b;
		if (a.size() != b.size())
			return false;
		switch (type.elementType().typeEnum()) {
		case BOOL:
			return ((List<Boolean>) a).equals((List<Boolean>) b);
		case INT8:
			return ((List<Byte>) a).equals((List<Byte>) b);
		case INT16:
			return ((List<Short>) a).equals((List<Short>) b);
		case INT32:
			return ((List<Integer>) a).equals((List<Integer>) b);
		case INT64:
			return ((List<Long>) a).equals((List<Long>) b);
		case FLOAT32:
			return ((List<Float>) a).equals((List<Float>) b);
		case FLOAT64:
			return ((List<Double>) a).equals((List<Double>) b);
		case STRING:
			return ((List<String>) a).equals((List<String>) b);
		default:
			return areObjectListsEqual(a, b, type.elementType());
		}
	}

	private static boolean areObjectListsEqual(
			final List<?> a,
			final List<?> b,
			final Type elemType) {
		if (a == null || b == null || a == b)
			return a == b;
		if (a.size() != b.size())
			return false;
		if (a.getClass() != b.getClass())
			return false;
		final Iterator<?> itA = a.iterator();
		final Iterator<?> itB = b.iterator();
		while (itA.hasNext()) {
			final Object xA = itA.next();
			final Object xB = itB.next();
			if (!areObjectsEqual(xA, xB, elemType)) {
				return false;
			}
		}
		return true;
	}

}
