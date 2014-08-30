package se.culvertsoft.mgen.api.model;

import java.lang.reflect.Array;

/**
 * Represents an Array in the MGen compiler's data model
 */
public class ArrayType extends ListOrArrayType {

	@Override
	public String fullName() {
		return "array[" + elementType().fullName() + "]";
	}

	@Override
	public String shortName() {
		return "array[" + elementType().shortName() + "]";
	}

	@Override
	public boolean isLinked() {
		return elementType().isLinked();
	}

	/**
	 * Creates a new array instance of this class. Intended for use outside the
	 * compiler, primarily during deserialization.
	 */
	public Object newInstance(final int n) {
		switch (elementType().typeEnum()) {
		case BOOL:
			return n > 0 ? new boolean[n] : EMPTY_BOOL_ARRAY;
		case INT8:
			return n > 0 ? new byte[n] : EMPTY_INT8_ARRAY;
		case INT16:
			return n > 0 ? new short[n] : EMPTY_INT16_ARRAY;
		case INT32:
			return n > 0 ? new int[n] : EMPTY_INT32_ARRAY;
		case INT64:
			return n > 0 ? new long[n] : EMPTY_INT64_ARRAY;
		case FLOAT32:
			return n > 0 ? new float[n] : EMPTY_FLOAT32_ARRAY;
		case FLOAT64:
			return n > 0 ? new double[n] : EMPTY_FLOAT64_ARRAY;
		case STRING:
			return n > 0 ? new String[n] : EMPTY_STRING_ARRAY;
		default:
			final Class<?> elemClass = elementType().classOf();
			return elemClass != null ? Array.newInstance(elemClass, n) : null;
		}
	}

	@Override
	public boolean containsUserDefinedType() {
		return elementType().containsUserDefinedType();
	}

	@Override
	public Class<?> classOf() {
		return m_cls;
	}

	private Class<?> doClassOf() {

		switch (elementType().typeEnum()) {
		case BOOL:
			return boolean[].class;
		case INT8:
			return byte[].class;
		case INT16:
			return short[].class;
		case INT32:
			return int[].class;
		case INT64:
			return long[].class;
		case FLOAT32:
			return float[].class;
		case FLOAT64:
			return double[].class;
		case STRING:
			return String[].class;
		default:
			final Class<?> elemCls = elementType().classOf();
			return elemCls != null ? Array.newInstance(elemCls, 0).getClass() : null;
		}
	}

	private final Class<?> m_cls;

	public ArrayType(final Type elementType) {
		super(TypeEnum.ARRAY, elementType);
		m_cls = doClassOf();
	}

	public static final Object EMPTY_BOOL_ARRAY = new boolean[0];
	public static final Object EMPTY_INT8_ARRAY = new byte[0];
	public static final Object EMPTY_INT16_ARRAY = new short[0];
	public static final Object EMPTY_INT32_ARRAY = new int[0];
	public static final Object EMPTY_INT64_ARRAY = new long[0];
	public static final Object EMPTY_FLOAT32_ARRAY = new float[0];
	public static final Object EMPTY_FLOAT64_ARRAY = new double[0];
	public static final Object EMPTY_STRING_ARRAY = new String[0];

}
