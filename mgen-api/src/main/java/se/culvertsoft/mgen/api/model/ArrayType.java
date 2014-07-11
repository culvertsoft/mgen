package se.culvertsoft.mgen.api.model;

/**
 * Interface for array types
 */
public interface ArrayType extends ListOrArrayType {

	public static final Object EMPTY_BOOL_ARRAY = new boolean[0];
	public static final Object EMPTY_INT8_ARRAY = new byte[0];
	public static final Object EMPTY_INT16_ARRAY = new short[0];
	public static final Object EMPTY_INT32_ARRAY = new int[0];
	public static final Object EMPTY_INT64_ARRAY = new long[0];
	public static final Object EMPTY_FLOAT32_ARRAY = new float[0];
	public static final Object EMPTY_FLOAT64_ARRAY = new double[0];
	public static final Object EMPTY_STRING_ARRAY = new String[0];

	/**
	 * Creates a new array instance of this class. Intended for use outside the
	 * compiler.
	 */
	public Object newInstance(final int n);

}
