package se.culvertsoft.mgen.api.model;

/**
 * One byte type tags intended to be used primarily outside the compiler for
 * verifying types when reading/writing binary streams.
 */
public interface BinaryTypeTag {
	public static final byte TAG_BOOL = (byte) 0;
	public static final byte TAG_INT8 = (byte) 1;
	public static final byte TAG_INT16 = (byte) 2;
	public static final byte TAG_INT32 = (byte) 3;
	public static final byte TAG_INT64 = (byte) 4;
	public static final byte TAG_FLOAT32 = (byte) 5;
	public static final byte TAG_FLOAT64 = (byte) 6;
	public static final byte TAG_STRING = (byte) 7;
	public static final byte TAG_LIST = (byte) 8;
	public static final byte TAG_MAP = (byte) 9;
	public static final byte TAG_CUSTOM = (byte) 10;
}
