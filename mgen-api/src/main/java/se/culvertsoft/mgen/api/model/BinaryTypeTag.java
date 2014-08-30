package se.culvertsoft.mgen.api.model;

/**
 * Single byte type tags used by the MGen BinaryWriter and BinaryReader for
 * indicating what data to write/read. One tag exists for each binary wire
 * format type. Note that some binary wire format tags can represent several
 * data model types (e.g. TAG_STRING represent Strings and Enums).
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
	public static final byte TAG_CLASS = (byte) 10;
}
