package se.culvertsoft.mgen.api.model;

public class BinaryTypeTag {
	public static final byte TAG_BOOL = (byte) 0;
	public static final byte TAG_INT8 = (byte) 1;
	public static final byte TAG_INT16 = (byte) 2;
	public static final byte TAG_INT32 = (byte) 3;
	public static final byte TAG_INT64 = (byte) 4;
	public static final byte TAG_FLOAT32 = (byte) 5;
	public static final byte TAG_FLOAT64 = (byte) 6;
	public static final byte TAG_STRING = (byte) 7;
	public static final byte TAG_ARRAY = (byte) 8;
	public static final byte TAG_LIST = (byte) 8;
	public static final byte TAG_MAP = (byte) 9;
	public static final byte TAG_CUSTOM = (byte) 10;
	public static final byte TAG_UNKNOWN = (byte) 10;
	public static final byte TAG_MGENBASE = (byte) 10;

	public static final byte TAG_BIT8 = (byte) 0x80;
	public static final byte TAG_BIT7 = (byte) (0x80 >> 1);

	public static final byte TAG_NULL_BIT = TAG_BIT8;
	public static final byte TAG_EMPTY_BIT = TAG_BIT7;

	public static final byte TAG_FALSE_BIT = TAG_BIT8;
	public static final byte TAG_TRUE_BIT = TAG_BIT7;

	public static final byte TAG_ZERO_BIT = TAG_BIT8;

}
